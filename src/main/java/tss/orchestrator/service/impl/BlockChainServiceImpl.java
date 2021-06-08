package tss.orchestrator.service.impl;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.*;
import tss.orchestrator.models.contracts.SmartInsurancePolicy;
import tss.orchestrator.repositories.AlertRepository;
import tss.orchestrator.repositories.SmartPolicyRepository;
import tss.orchestrator.repositories.UserRepository;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.constants.Constants;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.math.BigInteger;
import java.util.*;

@Service
public class BlockChainServiceImpl implements BlockChainService {

    private Web3j web3j;
    private Credentials credentials;
    private ContractGasProvider gasProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Override
    public BlockChainResponseTransfer createSmartPolicy(String privateKey, SmartPolicy smartPolicy) {
        this.initialize(privateKey);
        BlockChainResponseTransfer responseTransfer = this.deployContract(smartPolicy);
        return responseTransfer;
    }

    @Override
    public void updateSensorsData(SensorsDataDTO sensorsDataDTO){
        Optional<User> userOptional = userRepository.findById(sensorsDataDTO.getUserId());
        SmartPolicy smartPolicy = smartPolicyRepository.findById(sensorsDataDTO.getSmartPolicyId()).get();

        this.initialize(userOptional.get().getPrivateKey());
        BlockChainResponseTransfer responseTransfer = this.sendSensorsData(smartPolicy, sensorsDataDTO);
        if (smartPolicy.getState() != responseTransfer.getState()){
            smartPolicyRepository.setState(smartPolicy.getId(), responseTransfer.getState());
            smartPolicyRepository.setActivationTimestamp(smartPolicy.getId(), sensorsDataDTO.getDataTimeStamp());
        }

        //Alert management
        if (responseTransfer.getEvents() != null){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Alert alert = modelMapper.map(responseTransfer, Alert.class);
            alert.setSmartPolicy(smartPolicyRepository.findById(sensorsDataDTO.getSmartPolicyId()).get());
            alertRepository.save(alert);
        }
    }

    public void initialize(String privateKey){
        this.web3j = Web3j.build(new HttpService(Constants.HTTP_PROVIDER));
        this.credentials = Credentials.create (privateKey);
        this.gasProvider = new StaticGasProvider(BigInteger.valueOf(Constants.GAS_PRICE),
                BigInteger.valueOf(Constants.GAS_LIMIT));
    }

    public BlockChainResponseTransfer deployContract (SmartPolicy smartPolicy){
        BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();

        try {
            SmartInsurancePolicy contract = SmartInsurancePolicy.deploy(this.web3j,
                    credentials,
                    gasProvider,
                    Constants.TOKEN_ADDRESS,
                    smartPolicy.getClientAddress(),
                    smartPolicy.getInsuranceAddress(),
                    smartPolicy.getBrokerAddress(),
                    BigInteger.valueOf(smartPolicy.getContractPremium()).multiply(Constants.zeros), //200
                    BigInteger.valueOf(smartPolicy.getContractLiability()).multiply(Constants.zeros),  //1000
                    BigInteger.valueOf(smartPolicy.getInceptionTimestamp()), //1619647900
                    BigInteger.valueOf(smartPolicy.getExpiryTimestamp()), //1619747900
                    smartPolicy.getTerritorialScope(),
                    false)
                    .send();

            responseTransfer.setContractAddress(contract.getContractAddress());
            responseTransfer.setState(Constants.ContractState.INITIALIZED);

            contract.addShipment(BigInteger.valueOf(smartPolicy.getShipmentID()),
                    BigInteger.valueOf(smartPolicy.getShipmentLiability()).multiply(Constants.zeros))
                    .send();

            for (Map.Entry<String,Sensor> entry : ((HashMap<String, Sensor>) smartPolicy.getSensors()).entrySet()){
                Sensor sensor = entry.getValue();
                System.out.println(sensor.getContractContextId());
                TransactionReceipt transactionReceipt = contract.addSensor(BigInteger.valueOf(sensor.getContractContextId()),
                        BigInteger.valueOf(sensor.getContractContextId()))
                        .send();

                List<SmartInsurancePolicy.SensorAddedEventResponse> updatedEvents = contract.getSensorAddedEvents(transactionReceipt);

                System.out.println("ID:" + updatedEvents.get(0).ID);
                System.out.println("sensorType:" + updatedEvents.get(0).sensorType);

                transactionReceipt = contract.addConditionLevel(BigInteger.valueOf(sensor.getLevelDepth()),
                        BigInteger.valueOf(sensor.getContractContextId()),
                        BigInteger.valueOf(sensor.getLevelMinimumRange()).multiply(Constants.zeros),
                        BigInteger.valueOf(sensor.getLevelMaximumRange()).multiply(Constants.zeros),
                        BigInteger.valueOf(sensor.getPercentualWeight()))
                        .send();

                List<SmartInsurancePolicy.ConditionLevelAddedEventResponse> updatedEvents2 = contract.getConditionLevelAddedEvents(transactionReceipt);

                System.out.println("ID:" + updatedEvents2.get(0).ID);
                System.out.println("conditionLevelCount:" + updatedEvents2.get(0).conditionLevelCount);
                System.out.println("dataRangeMax:" + updatedEvents2.get(0).dataRangeMax);
                System.out.println("dataRangeMin:" + updatedEvents2.get(0).dataRangeMin);
                System.out.println("percentualWeight:" + updatedEvents2.get(0).percentualWeight);
            }

            contract.fundContract(BigInteger.valueOf(100)).send();
            responseTransfer.setState(Constants.ContractState.FUNDED);

            return responseTransfer;

        } catch (Exception e) {
            responseTransfer.setError(e.getMessage());
            return responseTransfer;
        }
    }

    public BlockChainResponseTransfer sendSensorsData(SmartPolicy smartPolicy, SensorsDataDTO sensorsDataDTO){
        BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();
        try {
            SmartInsurancePolicy contract = SmartInsurancePolicy.load(
                    smartPolicy.getContractAddress(),
                    web3j, credentials,
                    gasProvider);

            responseTransfer.setState(smartPolicy.getState());

            if(smartPolicy.getState().equals(Constants.ContractState.FUNDED)){
                contract.activateContract(BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp())).send();
                responseTransfer.setState(Constants.ContractState.ACTIVATED);
            }

            for (Map.Entry<String, Long> entry : sensorsDataDTO.getSensorData().entrySet()){
                int id = Constants.SensorType.valueOf(entry.getKey().toUpperCase(Locale.ROOT)).ordinal();
                TransactionReceipt transactionReceipt = contract.updateSensor(BigInteger.valueOf(id),
                        BigInteger.valueOf(entry.getValue()).multiply(Constants.zeros),
                        BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp()))
                        .send();

                System.out.println("ID update: " + id);

                List<SmartInsurancePolicy.SensorUpdatedEventResponse> updatedEvents = contract.getSensorUpdatedEvents(transactionReceipt);

                System.out.println(updatedEvents.get(0).levelID.intValue());

                if(updatedEvents.get(0).levelID.intValue() != -1){
                    if (responseTransfer.getEvents() == null){
                        responseTransfer.setEvents(new HashMap<>());
                    }
                    SensorEvents sensorEvents = new SensorEvents();
                    sensorEvents.setType(Constants.SensorType.values()[updatedEvents.get(0).sensorType.intValue()].name());
                    sensorEvents.setLevelID(updatedEvents.get(0).levelID);
                    sensorEvents.setUpdatedData(updatedEvents.get(0).updatedData.divide(Constants.zeros));
                    sensorEvents.setUpdatedDataExcess(updatedEvents.get(0).updatedDataExcess.divide(Constants.zeros));
                    sensorEvents.setLevelExcessTime(updatedEvents.get(0).levelExcessTime);
                    sensorEvents.setContractReserve(updatedEvents.get(0).contractReserve.divide(Constants.zeros));
                    responseTransfer.getEvents().put(Constants.SensorType.values()[updatedEvents.get(0).sensorType.intValue()], sensorEvents);
                }
            }
            return responseTransfer;

        } catch(Exception e){
            responseTransfer.setError(e.getMessage());
            return responseTransfer;
        }
    }

    public BlockChainResponseTransfer deactivate(SmartPolicy smartPolicy) {
        BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();

        try{
            SmartInsurancePolicy contract = SmartInsurancePolicy.load(
                    smartPolicy.getContractAddress(),
                    web3j, credentials,
                    gasProvider);

            contract.deactivateContract(BigInteger.valueOf(smartPolicy.getDeactivationTimestamp())).send();

            responseTransfer.setDeactivationTimestamp(smartPolicy.getDeactivationTimestamp());

            return responseTransfer;

        }catch (Exception e){
            responseTransfer.setError(e.getMessage());
            return responseTransfer;
        }
    }
}
