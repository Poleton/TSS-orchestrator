package tss.orchestrator.service;

import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import tss.orchestrator.models.SmartPolicy;

public interface BlockChainService {
    public String deployContract (SmartPolicy smartPolicy);
    public String sendSensorsData (SmartPolicy smartPolicy);
}
