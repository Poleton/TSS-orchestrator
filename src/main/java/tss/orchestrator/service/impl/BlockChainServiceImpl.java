package tss.orchestrator.service.impl;


import org.web3j.abi.datatypes.Int;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.contracts.Insurance_policy;
import tss.orchestrator.models.contracts.SmartContract;
import tss.orchestrator.models.contracts.SmartInsurancePolicy;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.constants.Constants;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

            //Deploy contract to address specified by wallet
            SmartInsurancePolicy contract = SmartInsurancePolicy.deploy(this.web3j,
                    credentials,
                    gasProvider,
                    Constants.ADDRESS_ACCOUNT,
                    Constants.ADDRESS_ACCOUNT,
                    Constants.ADDRESS_ACCOUNT,
                    BigInteger.valueOf(200),
                    BigInteger.valueOf(1000),
                    BigInteger.valueOf(1619647900),
                    BigInteger.valueOf(1619747900),
                    "hola").send();
            //Het the address
            return contract.getContractAddress();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "F";
        }
    }

    public String sendSensorsData(SmartPolicy smartPolicy){
        try {
            String a = "";

            SmartInsurancePolicy contract = SmartInsurancePolicy.load(
                    smartPolicy.getContractAddress(),
                    web3j, credentials,
                    gasProvider);
            System.out.println("Load smart contract done!");
            contract.addShipment(BigInteger.valueOf(1),BigInteger.valueOf(500)).send();
            a = contract.toString();

            return a;
        } catch(Exception e){
            e.printStackTrace();
            return "F";
        }
    }
}
