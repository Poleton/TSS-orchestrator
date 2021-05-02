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

import java.math.BigInteger;

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

    public String deployContract (SmartPolicy smartPolicy){

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

            contract.addShipment(BigInteger.valueOf(smartPolicy.getSmartId()),
                    BigInteger.valueOf(smartPolicy.getShipmentLiability()))
                    .send();

            for(int i = 0; i < smartPolicy.getSensorID().size(); i++){
                contract.addSensor(BigInteger.valueOf((Integer) smartPolicy.getSensorID().get(i)),
                        BigInteger.valueOf((Integer) smartPolicy.getSensorType().get(i)))
                        .send();
            }

            contract.addConditionLevel(BigInteger.valueOf(smartPolicy.getLevelDepth()),
                    BigInteger.valueOf(smartPolicy.getLevelType()),
                    BigInteger.valueOf(smartPolicy.getLevelMinimumRange()),
                    BigInteger.valueOf(smartPolicy.getLevelMaximumRange()),
                    BigInteger.valueOf(smartPolicy.getPercentualWeight()))
                    .send();

            //falta el funding

            return contract.getContractAddress();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "F";
        }
    }

    public String sendSensorsData(SmartPolicy smartPolicy, SensorsDataDTO sensorsDataDTO){
        try {

            String a = "";

            SmartInsurancePolicy contract = SmartInsurancePolicy.load(
                    smartPolicy.getContractAddress(),
                    web3j, credentials,
                    gasProvider);

            if(smartPolicy.getState().equals(Constants.ContractState.FUNDED)){
                contract.activateContract(BigInteger.valueOf(smartPolicy.getActivationTimestamp())).send();
            }

            for(int i = 0; i < smartPolicy.getSensorID().size(); i++){

                int sensorID = smartPolicy.getShipmentID()*10 + (Integer) smartPolicy.getSensorID().get(i);
                contract.updateSensor(BigInteger.valueOf(sensorID),
                        BigInteger.valueOf(sensorsDataDTO.getSensorData().get(i)),
                        BigInteger.valueOf(sensorsDataDTO.getDataTimeStamp()))
                        .send();
            }

            return a;
        } catch(Exception e){
            e.printStackTrace();
            return "F";
        }
    }
}
