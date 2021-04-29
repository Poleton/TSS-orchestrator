package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import tss.orchestrator.api.BlockChainRestApi;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.helpers.TimeHelper;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class BlockChainRestController implements BlockChainRestApi {

    @Autowired
    BlockChainService blockChainService;

    @Override
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO){



        //blockChainService.sendSensorsData();

        return ResponseEntity.accepted().build();
    }

}
