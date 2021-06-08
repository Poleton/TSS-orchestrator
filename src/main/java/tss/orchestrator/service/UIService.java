package tss.orchestrator.service;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;
import tss.orchestrator.models.Alert;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.transfers.UIResponseTransfer;

import java.util.HashMap;
import java.util.List;

public interface UIService {
    //POLICIES
    UIResponseTransfer retrieveAllPolicies(int userId);

    UIResponseTransfer createPolicy(int userId, PolicyDTO policyDTO);

    //SMART POLICIES
    UIResponseTransfer createSmartPolicy(int userId, SmartPolicyDTO smartPolicyDTO);

    UIResponseTransfer retrieveAllSmartPolicies( int userId);

    UIResponseTransfer getAlerts(int userId, int smartId);

    UIResponseTransfer deactivateSmartPolicy(int userId, int smartId);

    UIResponseTransfer hasNewAlerts(int userId, String data);

}
