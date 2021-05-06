package tss.orchestrator.service.impl;


import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.Sensor;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.contracts.SmartInsurancePolicy;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.constants.Constants;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@Service
public class BlockChainServiceImpl implements BlockChainService {

    private Web3j web3j;
    private Credentials credentials;
    private ContractGasProvider gasProvider;

    public void initialize(String privateKey){
        this.web3j = Web3j.build(new HttpService(Constants.HTTP_PROVIDER));
        this.credentials = Credentials.create (privateKey);
        this.gasProvider = new StaticGasProvider(BigInteger.valueOf(Constants.GAS_PRICE),
                BigInteger.valueOf(Constants.GAS_LIMIT));
    }

    public BlockChainResponseTransfer deployContract (SmartPolicy smartPolicy){
        BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();

        try {
            //hacer primero el deploy del token y pillar su direccion
            //luego hay que pasarsela como parametro en el deploy del contrato

            SmartInsurancePolicy contract = SmartInsurancePolicy.deploy(this.web3j,
                    credentials,
                    gasProvider,
                    Constants.TOKEN_ADDRESS,
                    smartPolicy.getClientAddress(),
                    smartPolicy.getInsuranceAddress(),
                    smartPolicy.getBrokerAddress(),
                    BigInteger.valueOf(smartPolicy.getContractPremium()), //200
                    BigInteger.valueOf(smartPolicy.getContractLiability()),  //1000
                    BigInteger.valueOf(smartPolicy.getInceptionTimestamp()), //1619647900
                    BigInteger.valueOf(smartPolicy.getExpiryTimestamp()), //1619747900
                    smartPolicy.getTerritorialScope())
                    .send();

            responseTransfer.setContractAddress(contract.getContractAddress());
            responseTransfer.setState(Constants.ContractState.INITIALIZED);

            contract.addShipment(BigInteger.valueOf(smartPolicy.getShipmentID()),
                    BigInteger.valueOf(smartPolicy.getShipmentLiability()))
                    .send();

            for (Map.Entry<String,Sensor> entry : ((HashMap<String, Sensor>) smartPolicy.getSensors()).entrySet()){
                Sensor sensor = entry.getValue();
                System.out.println(sensor.getContractContextId());
                contract.addSensor(BigInteger.valueOf(sensor.getContractContextId()),
                        BigInteger.valueOf(sensor.getContractContextId()))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(sensor.getLevelDepth()),
                        BigInteger.valueOf(sensor.getContractContextId()),
                        BigInteger.valueOf(sensor.getLevelMinimumRange()),
                        BigInteger.valueOf(sensor.getLevelMaximumRange()),
                        BigInteger.valueOf(sensor.getPercentualWeight()))
                        .send();
            }

            contract.fundContract(BigInteger.valueOf(100)).send();
            responseTransfer.setState(Constants.ContractState.FUNDED);

            return responseTransfer;

        } catch (Exception ex) {
            ex.printStackTrace();
            return responseTransfer;
        }
    }

    public BlockChainResponseTransfer sendSensorsData(SmartPolicy smartPolicy, SensorsDataDTO sensorsDataDTO){
        try {

            BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();
            responseTransfer.setState(smartPolicy.getState());

            SmartInsurancePolicy contract = SmartInsurancePolicy.load(
                    smartPolicy.getContractAddress(),
                    web3j, credentials,
                    gasProvider);

            if(smartPolicy.getState().equals(Constants.ContractState.FUNDED)){
                contract.activateContract(BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp())).send();
                responseTransfer.setState(Constants.ContractState.ACTIVATED);
            }

            responseTransfer.setEvents(new HashMap<>());
            for (Map.Entry<String, Long> entry : sensorsDataDTO.getSensorData().entrySet()){
                int id = Constants.SensorType.valueOf(entry.getKey().toUpperCase(Locale.ROOT)).ordinal();
                TransactionReceipt transactionReceipt = contract.updateSensor(BigInteger.valueOf(id),
                        BigInteger.valueOf(entry.getValue()),
                        BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp()))
                        .send();

                List<SmartInsurancePolicy.SensorUpdatedEventResponse> updatedEvents = contract.getSensorUpdatedEvents(transactionReceipt);

                System.out.println(updatedEvents.get(0).levelID);
                System.out.println(updatedEvents.get(0).updatedData);
                System.out.println(updatedEvents.get(0).updatedDataExcess);
                System.out.println(updatedEvents.get(0).levelExcessTime);
                System.out.println(updatedEvents.get(0).contractReserve);
                System.out.println(updatedEvents.get(0).sensorType.intValue());

                if(updatedEvents.get(0).levelID.intValue() != -1){
                    Map<String, BigInteger> map = new HashMap<>();
                    map.put("levelID", updatedEvents.get(0).levelID);
                    map.put("updatedData", updatedEvents.get(0).updatedData);
                    map.put("updatedDataExcess", updatedEvents.get(0).updatedDataExcess);
                    map.put("levelExcessTime", updatedEvents.get(0).levelExcessTime);
                    map.put("contractReserve", updatedEvents.get(0).contractReserve);
                    responseTransfer.getEvents().put(Constants.SensorType.values()[updatedEvents.get(0).sensorType.intValue()], map);
                }
            }
            return responseTransfer;

        } catch(Exception e){
            e.printStackTrace();
            return new BlockChainResponseTransfer();
        }
    }
}
