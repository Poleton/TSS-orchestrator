package tss.orchestrator.service.impl;


import org.springframework.stereotype.Component;
import org.web3j.abi.EventValues;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.Sensor;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.contracts.SmartInsurancePolicy;
import tss.orchestrator.models.contracts.TSSDollar;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.constants.Constants;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

            contract.addShipment(BigInteger.valueOf(smartPolicy.getId()),
                    BigInteger.valueOf(smartPolicy.getShipmentLiability()))
                    .send();

            Iterator it = smartPolicy.getSensors().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                Sensor sensor = (Sensor) pair.getValue();

                contract.addSensor(BigInteger.valueOf(sensor.getId()),
                        BigInteger.valueOf(sensor.getType()))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(sensor.getLevelDepth()),
                        BigInteger.valueOf(sensor.getType()),
                        BigInteger.valueOf(sensor.getLevelMinimumRange()),
                        BigInteger.valueOf(sensor.getLevelMaximumRange()),
                        BigInteger.valueOf(sensor.getPercentualWeight()))
                        .send();
            }


            //falta el funding

            responseTransfer.setState(Constants.ContractState.ACTIVATED);

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
                contract.activateContract(BigInteger.valueOf(smartPolicy.getActivationTimestamp())).send();
                responseTransfer.setState(Constants.ContractState.ACTIVATED);
            }

            Iterator it = sensorsDataDTO.getSensorData().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                TransactionReceipt transactionReceipt = contract.updateSensor(BigInteger.valueOf((Integer) pair.getKey()),
                        BigInteger.valueOf((Long) pair.getValue()),
                        BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp()))
                        .send();

                List<SmartInsurancePolicy.SensorUpdatedEventResponse> updatedEvents = contract.getSensorUpdatedEvents(transactionReceipt);

                responseTransfer.setLevelId(updatedEvents.get(0).levelID);
                responseTransfer.setSensorType(updatedEvents.get(0).sensorType);
                responseTransfer.setSensorData(updatedEvents.get(0).updatedData);
                responseTransfer.setDataExcess(updatedEvents.get(0).updatedDataExcess);
                responseTransfer.setLevelExcessTime(updatedEvents.get(0).levelExcessTime);
                responseTransfer.setContractReserve(updatedEvents.get(0).contractReserve);
                responseTransfer.setState(Constants.ContractState.values()[contract.contractState().send().intValue()]);
            }
            return responseTransfer;

        } catch(Exception e){
            e.printStackTrace();
            return new BlockChainResponseTransfer();
        }
    }
}
