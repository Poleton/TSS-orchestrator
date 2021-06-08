package tss.orchestrator.controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tss.orchestrator.api.UIRestApi;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;
import tss.orchestrator.service.UIService;

import tss.orchestrator.utils.transfers.UIResponseTransfer;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class UIRestController implements UIRestApi {

    @Autowired
    private UIService uiServiceImpl;

    @Override
    public ResponseEntity<List<Object>> retrieveAllPolicies(@PathVariable int userId) {
        UIResponseTransfer responseTransfer = uiServiceImpl.retrieveAllPolicies(userId);
        return new ResponseEntity<>(responseTransfer.getList(), responseTransfer.getHttpStatus());
    }

    @Override
    public ResponseEntity<String> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO) {
        UIResponseTransfer responseTransfer = uiServiceImpl.createPolicy(userId, policyDTO);

        if (responseTransfer.isError()){
            return new ResponseEntity<>(responseTransfer.getMessage(), responseTransfer.getHttpStatus());
        }else{
            return ResponseEntity.created(responseTransfer.getLocation()).build();
        }
    }

    @Override
    public ResponseEntity<Object> createSmartPolicy(int userId, SmartPolicyDTO smartPolicyDTO) {
        UIResponseTransfer responseTransfer = uiServiceImpl.createSmartPolicy(userId, smartPolicyDTO);
        return ResponseEntity.created(responseTransfer.getLocation()).build();
    }

    @Override
    public ResponseEntity<List<Object>> retrieveAllSmartPolicies (int userId) {
        UIResponseTransfer responseTransfer = uiServiceImpl.retrieveAllSmartPolicies(userId);
        return new ResponseEntity<>(responseTransfer.getList(), responseTransfer.getHttpStatus());
    }

    @Override
    public ResponseEntity<List<Object>> getAlerts(int userId, int smartId) throws Exception {
        UIResponseTransfer responseTransfer = uiServiceImpl.getAlerts(userId, smartId);
        return new ResponseEntity<>(responseTransfer.getList(), responseTransfer.getHttpStatus());
    }

    @Override
    public ResponseEntity<Object> deactivateSmartPolicy(int userId, int smartId) {
        UIResponseTransfer responseTransfer = uiServiceImpl.deactivateSmartPolicy(userId, smartId);
        return new ResponseEntity<>(responseTransfer.getMessage(), responseTransfer.getHttpStatus());
    }

    @Override
    public ResponseEntity<HashMap<String,Integer>> hasNewAlerts(int userId, String data) throws JSONException {
        UIResponseTransfer responseTransfer = uiServiceImpl.hasNewAlerts(userId, data);
        return new ResponseEntity<>(responseTransfer.getNewAlerts(), responseTransfer.getHttpStatus());
    }
}
