package tss.orchestrator.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tss.orchestrator.api.UIRestApi;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;
import tss.orchestrator.models.Alert;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.PolicyRepository;
import tss.orchestrator.service.SmartPolicyRepository;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.service.impl.BlockChainServiceImpl;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UIRestController implements UIRestApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Autowired
    private BlockChainServiceImpl blockChainService;


    @Override
    public List<Policy> retrieveAllPolicies(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            //throw new UserNotFoundException("userId-" + userId);
        }

        //return System.out.println("HELLO WORLD!");
        return userOptional.get().getPolicies();
    }

    @Override
    public ResponseEntity<Object> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        ModelMapper modelMapper = new ModelMapper();
        Policy policy = modelMapper.map(policyDTO, Policy.class);
        policy.setUser(userOptional.get());

        policyRepository.save(policy);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(policy.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Object> createSmartPolicy(int userId, SmartPolicyDTO smartPolicyDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Policy> policyOptional = policyRepository.findById(smartPolicyDTO.getPolicyId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SmartPolicy smartPolicy = modelMapper.map(smartPolicyDTO, SmartPolicy.class);
        modelMapper.map(policyOptional.get(), smartPolicy);

        /*
        blockChainService.initialize(userOptional.get().getPrivateKey());

        BlockChainResponseTransfer responseTransfer = blockChainService.deployContract(smartPolicy);

        smartPolicy.setContractAddress(responseTransfer.getContractAddress());
        smartPolicy.setState(responseTransfer.getState());
        */

        smartPolicyRepository.save(smartPolicy);
        //System.out.println(smartPolicy.getSensors().toString());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(smartPolicy.getId()).toUri();

        return ResponseEntity.created(location).build();


    }

    public List<SmartPolicy> retrieveAllSmartPolicies (int userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getSmartPolicies();
        }else throw new Exception("Missing user");
    }

    @Override
    public ResponseEntity<List<Alert>> getAlerts(int userId, int smartId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartId);

        if(user.isPresent() && smartPolicy.isPresent()){
            return new ResponseEntity<>(smartPolicy.get().getAlerts(), HttpStatus.OK);
        }
        else{
            throw new Exception("User or SmartPolicy not found");
        }

    }
}
