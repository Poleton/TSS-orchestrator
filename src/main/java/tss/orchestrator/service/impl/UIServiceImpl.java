package tss.orchestrator.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SmartPolicyDTO;
import tss.orchestrator.models.*;
import tss.orchestrator.repositories.AlertRepository;
import tss.orchestrator.repositories.PolicyRepository;
import tss.orchestrator.repositories.SmartPolicyRepository;
import tss.orchestrator.repositories.UserRepository;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.service.LoginService;
import tss.orchestrator.service.UIService;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.helpers.Validator;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;
import tss.orchestrator.utils.transfers.UIResponseTransfer;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@Service
public class UIServiceImpl implements UIService {

    @Autowired
    private LoginService loginServiceImpl;

    @Autowired
    private BlockChainService blockChainServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Autowired
    private AlertRepository alertRepository;

    private Validator validate = new Validator();

    @Override
    public UIResponseTransfer retrieveAllPolicies(int userId) {
        UIResponseTransfer responseTransfer = new UIResponseTransfer();

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()){
            responseTransfer.setList(new ArrayList<Object>(userOptional.get().getPolicies()));
        }else{
            responseTransfer.setHttpStatus(HttpStatus.NOT_FOUND);
            responseTransfer.setError(true);
        }
        return responseTransfer;
    }

    @Override
    public UIResponseTransfer createPolicy(int userId, PolicyDTO policyDTO) {
        UIResponseTransfer responseTransfer = new UIResponseTransfer();

        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) {
            Instant instant = Instant.now();
            if(policyDTO.getExpiryTimestamp()<= instant.getEpochSecond()){
                responseTransfer.setError(true);
                responseTransfer.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseTransfer.setMessage("Wrong expiry timestamp");
            }
            else if(!validate.cifValidator(policyDTO.getPolicyHolderCIF())){
                responseTransfer.setError(true);
                responseTransfer.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseTransfer.setMessage("Incorrect format");
            }
            ModelMapper modelMapper = new ModelMapper();
            Policy policy = modelMapper.map(policyDTO, Policy.class);
            policy.setUser(userOptional.get());

            policy.setInceptionTimestamp(instant.getEpochSecond());

            policyRepository.save(policy);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(policy.getId())
                    .toUri();

            responseTransfer.setLocation(location);

        }
        else{
            responseTransfer.setError(true);
            responseTransfer.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return responseTransfer;
    }

    @Override
    public UIResponseTransfer createSmartPolicy(int userId, SmartPolicyDTO smartPolicyDTO) {
        UIResponseTransfer uiResponseTransfer = new UIResponseTransfer();

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Policy> policyOptional = policyRepository.findById(smartPolicyDTO.getPolicyId());

        //Smart Policy build
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SmartPolicy smartPolicy = modelMapper.map(smartPolicyDTO, SmartPolicy.class);
        modelMapper.map(policyOptional.get(), smartPolicy);
        smartPolicy.setBrokerAddress(userOptional.get().getBrokerAddress());
        smartPolicy.setClientAddress(userOptional.get().getClientAddress());
        smartPolicy.setInsuranceAddress(userOptional.get().getInsuranceAddress());
        for (Map.Entry<String, Sensor> entry : ((HashMap<String, Sensor>) smartPolicy.getSensors()).entrySet()) {
            entry.getValue().setContractContextId(Constants.SensorType.valueOf(entry.getKey().toUpperCase(Locale.ROOT)).ordinal());
            entry.getValue().setType(entry.getKey());
        }
        smartPolicy.setId(null);

        //Blockchain interaction
        BlockChainResponseTransfer blockChainResponseTransfer = blockChainServiceImpl.createSmartPolicy(userOptional.get().getPrivateKey(), smartPolicy);

        //Set other parameters
        policyRepository.setIsSmart(policyOptional.get().getId(), true);
        smartPolicy.setContractAddress(blockChainResponseTransfer.getContractAddress());
        smartPolicy.setState(blockChainResponseTransfer.getState());
        smartPolicyRepository.save(smartPolicy);

        //Response
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(smartPolicy.getId()).toUri();
        uiResponseTransfer.setLocation(location);
        return uiResponseTransfer;
    }

    @Override
    public UIResponseTransfer retrieveAllSmartPolicies(int userId) {
        UIResponseTransfer responseTransfer = new UIResponseTransfer();

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()){
            responseTransfer.setList(new ArrayList<Object>(userOptional.get().getSmartPolicies()));
        }else{
            responseTransfer.setHttpStatus(HttpStatus.NOT_FOUND);
            responseTransfer.setError(true);
        }
        return responseTransfer;
    }

    @Override
    public UIResponseTransfer getAlerts(int userId, int smartId) {
        UIResponseTransfer responseTransfer = new UIResponseTransfer();

        Optional<User> user = userRepository.findById(userId);
        Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartId);

        if(smartPolicy.get().getAlerts().isEmpty()){
            responseTransfer.setError(true);
            responseTransfer.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        else if(user.isPresent() && smartPolicy.isPresent()){
            responseTransfer.setList(new ArrayList<Object>(smartPolicy.get().getAlerts()));
        }
        else{
            responseTransfer.setError(true);
            responseTransfer.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        return responseTransfer;
    }

    @Override
    public UIResponseTransfer deactivateSmartPolicy(int userId, int smartId) {
        UIResponseTransfer uiResponseTransfer = new UIResponseTransfer();

        Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartId);
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent() && smartPolicy.isPresent() && smartPolicy.get().getState() != Constants.ContractState.DEACTIVATED){
            Instant instant = Instant.now();
            long deactivationTimestamp = instant.getEpochSecond();
            smartPolicy.get().setDeactivationTimestamp(deactivationTimestamp);
            BlockChainResponseTransfer blockChainResponseTransfer = blockChainServiceImpl.deactivate(smartPolicy.get());

            if(blockChainResponseTransfer.getError() == null){
                smartPolicyRepository.setState(smartPolicy.get().getId(), Constants.ContractState.DEACTIVATED);
                smartPolicyRepository.setDeactivationTimestamp(smartPolicy.get().getId(), deactivationTimestamp);
            }else{
                System.out.println(blockChainResponseTransfer.getError());
                uiResponseTransfer.setError(true);
                uiResponseTransfer.setMessage(blockChainResponseTransfer.getError());
                uiResponseTransfer.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
            }
        }
        else {
            uiResponseTransfer.setError(true);
            uiResponseTransfer.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        return uiResponseTransfer;
    }

    @Override
    public UIResponseTransfer hasNewAlerts(int userId, String data) {
        UIResponseTransfer responseTransfer = new UIResponseTransfer();

        Optional<User> user = userRepository.findById(userId);
        JSONObject json = new JSONObject(data);

        HashMap<String,Integer> map = new HashMap<>();

        if(user.isPresent()){
            JSONArray keys = json.names();
            for(int i=0;i<keys.length();i++){
                int smartPolicyId = keys.getInt(i);
                int alertId = json.getInt(keys.getString(i));
                Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartPolicyId);
                if(smartPolicy.isPresent()) {
                    List<Alert> alerts = smartPolicy.get().getAlerts();
                    int j = alerts.size();
                    if (!alerts.isEmpty() && alerts.get(j - 1).getId() > alertId) {
                        if (alertId != 0){
                            int k= alerts.indexOf(alertRepository.findById(alertId).get());
                            map.put(keys.getString(i),j-k-1);
                        }else{
                            map.put(keys.getString(i),j);
                        }
                    } else {
                        map.put(keys.getString(i), 0);
                    }
                }else {
                    responseTransfer.setError(true);
                    responseTransfer.setHttpStatus(HttpStatus.BAD_REQUEST);
                }
            }
            responseTransfer.setNewAlerts(map);
        }else{
            responseTransfer.setError(true);
            responseTransfer.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return responseTransfer;
    }


}
