package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tss.orchestrator.api.UIRestApi;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.PolicyRepository;
import tss.orchestrator.service.UserRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UIRestController implements UIRestApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Override
    public List<Policy> retrieveAllPolicies(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            //throw new UserNotFoundException("userId-" + userId);
        }

        return userOptional.get().getPolicies();
    }

    @Override
    public ResponseEntity<Object> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO) {

        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            //throw new UserNotFoundException("userId-" + userId);
        }

        Policy policy = new Policy(policyDTO, userOptional.get());

        policyRepository.save(policy);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(policy.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
