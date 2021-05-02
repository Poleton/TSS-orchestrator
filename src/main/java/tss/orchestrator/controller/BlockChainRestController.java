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

        int listId = 0;
        for(int i = 0; i < smartPolicies.size(); i++){
            if(smartPolicies.get(i).getContractAddress() == sensorsDataDTO.getContractAddress()){
                listId = i;
            }
        }

        blockChainServiceImpl.initialize(userOptional.get().getPrivateKey());

        blockChainServiceImpl.sendSensorsData(smartPolicies.get(listId), sensorsDataDTO);

        return ResponseEntity.accepted().build();
    }



}
