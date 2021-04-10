package tss.orchestrator.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.models.Policy;

import java.util.List;

@RequestMapping("/users")
public interface UIRestControllerApi {

    @GetMapping("/{id}/policies")
    public List<Policy> retrieveAllPolicies(@PathVariable int id);

    @PostMapping("/{id}/policies")
    public ResponseEntity<Object> createPolicy(@PathVariable int id, @RequestBody PolicyDTO policyDTO);
}
