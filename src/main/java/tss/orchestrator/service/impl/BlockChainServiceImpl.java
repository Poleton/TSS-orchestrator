package tss.orchestrator.service.impl;


import org.springframework.stereotype.Component;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.api.dto.SensorsDataDTO;
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
import java.util.Iterator;
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

        try {
            SmartInsurancePolicy contract = SmartInsurancePolicy.deploy(this.web3j,
                    credentials,
                    gasProvider,
                    smartPolicy.getClientAddress(),
                    smartPolicy.getInsuranceAddress(),
                    smartPolicy.getBrokerAddress(),
                    BigInteger.valueOf(smartPolicy.getContractPremium()), //200
                    BigInteger.valueOf(smartPolicy.getContractLiability()),  //1000
                    BigInteger.valueOf(smartPolicy.getInceptionTimestamp()), //1619647900
                    BigInteger.valueOf(smartPolicy.getExpiryTimestamp()), //1619747900
                    smartPolicy.getTerritorialScope())
                    .send();

            contract.addShipment(BigInteger.valueOf(smartPolicy.getId()),
                    BigInteger.valueOf(smartPolicy.getShipmentLiability()))
                    .send();
/*
            if(smartPolicy.getTemperatureSensorID() != null){
                contract.addSensor(BigInteger.valueOf(smartPolicy.getTemperatureSensorID()),
                        BigInteger.valueOf(0))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(smartPolicy.getTemperatureLevelDepth()),
                        BigInteger.valueOf(0),
                        BigInteger.valueOf(smartPolicy.getTemperatureLevelMinimumRange()),
                        BigInteger.valueOf(smartPolicy.getTemperatureLevelMaximumRange()),
                        BigInteger.valueOf(smartPolicy.getTemperaturePercentualWeight()))
                        .send();
            }

            if(smartPolicy.getPressureSensorID() != null){
                contract.addSensor(BigInteger.valueOf(smartPolicy.getPressureSensorID()),
                        BigInteger.valueOf(1))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(smartPolicy.getTemperatureLevelDepth()),
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(smartPolicy.getPressureLevelMinimumRange()),
                        BigInteger.valueOf(smartPolicy.getPressureLevelMaximumRange()),
                        BigInteger.valueOf(smartPolicy.getPressurePercentualWeight()))
                        .send();
            }

            if(smartPolicy.getAccelerationSensorID() != null){
                contract.addSensor(BigInteger.valueOf(smartPolicy.getAccelerationSensorID()),
                        BigInteger.valueOf(2))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(smartPolicy.getTemperatureLevelDepth()),
                        BigInteger.valueOf(2),
                        BigInteger.valueOf(smartPolicy.getAccelerationLevelMinimumRange()),
                        BigInteger.valueOf(smartPolicy.getAccelerationLevelMaximumRange()),
                        BigInteger.valueOf(smartPolicy.getAccelerationPercentualWeight()))
                        .send();
            }

            if(smartPolicy.getHumiditySensorID() != null){
                contract.addSensor(BigInteger.valueOf(smartPolicy.getHumiditySensorID()),
                        BigInteger.valueOf(3))
                        .send();
                contract.addConditionLevel(BigInteger.valueOf(smartPolicy.getTemperatureLevelDepth()),
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(smartPolicy.getHumidityLevelMinimumRange()),
                        BigInteger.valueOf(smartPolicy.getHumidityLevelMaximumRange()),
                        BigInteger.valueOf(smartPolicy.getHumidityPercentualWeight()))
                        .send();
            }
*/
            //falta el funding

            BlockChainResponseTransfer responseTransfer = new BlockChainResponseTransfer();
            responseTransfer.setState(Constants.ContractState.FUNDED);
            responseTransfer.setContractAddress(contract.getContractAddress());

            return responseTransfer;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new BlockChainResponseTransfer();
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
                contract.updateSensor(BigInteger.valueOf((Integer) pair.getKey()),
                        BigInteger.valueOf((Long) pair.getValue()),
                        BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp()))
                        .send();
            }

            return responseTransfer;
        } catch(Exception e){
            e.printStackTrace();
            return new BlockChainResponseTransfer();
        }
    }
}
