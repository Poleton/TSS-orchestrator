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
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.PolicyRepository;
import tss.orchestrator.service.SmartPolicyRepository;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.service.impl.BlockChainServiceImpl;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.helpers.TimeHelper;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class BlockChainRestController implements BlockChainRestApi {

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @Autowired
    private UserRepository userRepository;



    @Override
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO){

        Optional<User> userOptional = userRepository.findById(sensorsDataDTO.getUserId());

        List<SmartPolicy> smartPolicies = userOptional.get().getSmartPolicies();

        int listId = 0;
        for(int i = 0; i < smartPolicies.size(); i++){
            if(smartPolicies.get(i).getContractAddress() == sensorsDataDTO.getContractAddress()){
                listId = i;
            }
        }

        blockChainService.initialize(userOptional.get().getPrivateKey());

        blockChainService.sendSensorsData(smartPolicies.get(listId));

        return ResponseEntity.accepted().build();
    }

}
