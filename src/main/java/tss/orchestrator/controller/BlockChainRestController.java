package tss.orchestrator.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tss.orchestrator.api.BlockChainRestApi;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.models.Alert;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.AlertRepository;
import tss.orchestrator.service.SmartPolicyRepository;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.util.Optional;

@RestController
public class BlockChainRestController implements BlockChainRestApi {

    @Autowired
    private BlockChainService blockChainServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Override
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO){

        Optional<User> userOptional = userRepository.findById(sensorsDataDTO.getUserId());
        SmartPolicy smartPolicy = smartPolicyRepository.findById(sensorsDataDTO.getSmartPolicyId()).get();

        //Blockchain interaction
        blockChainServiceImpl.initialize(userOptional.get().getPrivateKey());
        BlockChainResponseTransfer responseTransfer = blockChainServiceImpl.sendSensorsData(smartPolicy, sensorsDataDTO);
        if (smartPolicy.getState() != responseTransfer.getState()){
            smartPolicyRepository.setState(smartPolicy.getId(), responseTransfer.getState());
            smartPolicyRepository.setActivationTimestamp(smartPolicy.getId(), sensorsDataDTO.getDataTimeStamp());
        }

        //Alert management
        if (!responseTransfer.getEvents().equals(null)){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Alert alert = modelMapper.map(responseTransfer, Alert.class);
            alert.setSmartPolicy(smartPolicyRepository.findById(sensorsDataDTO.getSmartPolicyId()).get());
            alertRepository.save(alert);
        }

        return ResponseEntity.accepted().build();
    }

}
