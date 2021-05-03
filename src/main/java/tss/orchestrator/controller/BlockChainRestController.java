package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tss.orchestrator.api.BlockChainRestApi;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.SmartPolicyRepository;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.util.List;
import java.util.Optional;

@RestController
public class BlockChainRestController implements BlockChainRestApi {

    @Autowired
    private BlockChainService blockChainServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Override
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO){

        Optional<User> userOptional = userRepository.findById(sensorsDataDTO.getUserId());

        List<SmartPolicy> smartPolicies = userOptional.get().getSmartPolicies();

        SmartPolicy smartPolicy = null;
        for(int i = 0; i < smartPolicies.size(); i++){
            if(smartPolicies.get(i).getContractAddress() == sensorsDataDTO.getContractAddress()){
                smartPolicy = smartPolicies.get(i);
            }
        }

        blockChainServiceImpl.initialize(userOptional.get().getPrivateKey());

        BlockChainResponseTransfer responseTransfer = blockChainServiceImpl.sendSensorsData(smartPolicy, sensorsDataDTO);

        if (smartPolicy.getState() != responseTransfer.getState()){
            smartPolicyRepository.setState(smartPolicy.getId(), responseTransfer.getState());
        }

        return ResponseEntity.accepted().build();
    }

}
