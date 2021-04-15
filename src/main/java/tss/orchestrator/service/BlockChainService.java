package tss.orchestrator.service;

import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

public interface BlockChainService {
    public EthBlockNumber getBlockNumber();

    public EthAccounts getEthAccounts();

    public EthGetTransactionCount getTransactionCount();

    public EthGetBalance getEthBalance();
}
