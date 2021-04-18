package tss.orchestrator.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.models.Policy;
import tss.orchestrator.utils.constants.Constants;

import java.util.List;

@RequestMapping(Constants.API_USERS)
public interface UIRestApi {

    @GetMapping(Constants.API_POLICIES)
    public List<Policy> retrieveAllPolicies(@PathVariable int userId);

    @PostMapping(Constants.API_POLICIES)
    public ResponseEntity<Object> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO);
}
