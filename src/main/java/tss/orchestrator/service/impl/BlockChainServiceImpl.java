package tss.orchestrator.service.impl;


import org.web3j.abi.datatypes.Int;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.contracts.Insurance_policy;
import tss.orchestrator.models.contracts.SmartContract;
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

    public BlockChainServiceImpl(String privateKey){
        this.web3j = Web3j.build(new HttpService(Constants.HTTP_PROVIDER));
        this.credentials = Credentials.create (privateKey);
        this.gasProvider = new StaticGasProvider(BigInteger.valueOf(Constants.GAS_PRICE),
                BigInteger.valueOf(Constants.GAS_LIMIT));
    }

    public String deployContract (SmartPolicy smartPolicy){

        int contractModel = smartPolicy.getContractModel();

        try {
            switch (contractModel){
                case 1:
                    //Deploy contract to address specified by wallet
                    Insurance_policy contract = Insurance_policy.deploy(this.web3j,
                            credentials,
                            gasProvider,
                            BigInteger.valueOf(1619647900),
                            BigInteger.valueOf(1619747900),
                            "hola").send();
                    //Het the address
                    return contract.getContractAddress();
                default:
                    return "F";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "F";
        }
    }

    public String sendSensorsData(SmartPolicy smartPolicy){
        try {
            String a = "";
            switch (smartPolicy.getContractModel()){
                case 1:
                    Insurance_policy contract = Insurance_policy.load(
                            smartPolicy.getContractAddress(),
                            web3j, credentials,
                            gasProvider);
                    System.out.println("Load smart contract done!");
                    contract.addSensor(BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1));
                    a = contract.toString();
            }

            return a;
        } catch(Exception e){
            e.printStackTrace();
            return "F";
        }
    }


    @Async
    public String sendTx() {
        String transactionHash = "";

        try {
            List inputParams = new ArrayList();
            List outputParams = new ArrayList();
            Function function = new Function("fuctionName", inputParams, outputParams);

            String encodedFunction = FunctionEncoder.encode(function);

            BigInteger nonce = BigInteger.valueOf(100);
            BigInteger gasprice = BigInteger.valueOf(100);
            BigInteger gaslimit = BigInteger.valueOf(100);

            Transaction transaction = Transaction.createFunctionCallTransaction("FROM_ADDRESS", nonce, gasprice, gaslimit, "TO_ADDRESS", encodedFunction);

            org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = web3j.ethSendTransaction(transaction).sendAsync().get();
            transactionHash = transactionResponse.getTransactionHash();

        } catch (Exception ex) {
            System.out.println(Constants.PLEASE_SUPPLY_REAL_DATA);
            return Constants.PLEASE_SUPPLY_REAL_DATA;
        }

        return transactionHash;
    }
}
