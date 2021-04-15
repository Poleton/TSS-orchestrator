package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import tss.orchestrator.api.BlockChainRestControllerApi;
import tss.orchestrator.service.impl.BlockChainServiceImpl;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.helpers.TimeHelper;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class BlockChainRestController implements BlockChainRestControllerApi {

    @Autowired
    BlockChainService blockChainService;

    @GetMapping(Constants.API_BLOCK)
    public Future<BlockChainResponseTransfer> getBlock() {
        BlockChainResponseTransfer blockChainResponseTransfer = new BlockChainResponseTransfer();
        Instant start = TimeHelper.start();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthBlockNumber result = blockChainService.getBlockNumber();
                blockChainResponseTransfer.setMessage(result.toString());
            } catch (Exception e) {
                blockChainResponseTransfer.setMessage(Constants.GENERIC_EXCEPTION);
            }
            return blockChainResponseTransfer;
        }).thenApplyAsync(result -> {
            result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

    @GetMapping(Constants.API_ACCOUNTS)
    public Future<BlockChainResponseTransfer> getAccounts() {
        BlockChainResponseTransfer blockChainResponseTransfer = new BlockChainResponseTransfer();
        Instant start = TimeHelper.start();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthAccounts result = blockChainService.getEthAccounts();
                blockChainResponseTransfer.setMessage(result.toString());
            } catch (Exception e) {
                blockChainResponseTransfer.setMessage(Constants.GENERIC_EXCEPTION);
            }
            return blockChainResponseTransfer;

        }).thenApplyAsync(result -> {
            result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

    @GetMapping(Constants.API_TRANSACTIONS)
    public Future<BlockChainResponseTransfer> getTransactions() {
        BlockChainResponseTransfer blockChainResponseTransfer = new BlockChainResponseTransfer();
        Instant start = TimeHelper.start();
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetTransactionCount result = blockChainService.getTransactionCount();
                blockChainResponseTransfer.setMessage(result.toString());
            } catch (Exception e) {
                blockChainResponseTransfer.setMessage(Constants.GENERIC_EXCEPTION);
            }
            return blockChainResponseTransfer;
        }).thenApplyAsync(result -> {
            result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

    @GetMapping(Constants.API_BALANCE)
    public Future<BlockChainResponseTransfer> getBalance() {
        BlockChainResponseTransfer blockChainResponseTransfer = new BlockChainResponseTransfer();
        Instant start = TimeHelper.start();
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetBalance result = blockChainService.getEthBalance();
                blockChainResponseTransfer.setMessage(result.toString());
            } catch (Exception e) {
                blockChainResponseTransfer.setMessage(Constants.GENERIC_EXCEPTION);
            }
            return blockChainResponseTransfer;
        }).thenApplyAsync(result -> {
            result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

}
