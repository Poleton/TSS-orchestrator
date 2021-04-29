package tss.orchestrator.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartContractDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.constants.Constants;

import java.util.List;

@RequestMapping(path=Constants.API_USERS)
public interface UIRestApi {

    //POLICIES
    @GetMapping(Constants.API_POLICIES)
    List<Policy> retrieveAllPolicies(@PathVariable int userId);


    @PostMapping(Constants.API_POLICIES)
    ResponseEntity<Object> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO);

    //SMART POLICIES
    @PostMapping( Constants.API_SMART_POLICIES)
    ResponseEntity<Object> createSmartPolicy(@PathVariable int userId, @RequestBody SmartPolicyDTO smartPolicyDTO);

    @GetMapping(Constants.API_SMART_POLICIES)
    List<SmartPolicy> retrieveAllSmartPolicies(@PathVariable int userId) throws Exception;


}
