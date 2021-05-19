package tss.orchestrator.controller;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import tss.orchestrator.models.*;
import tss.orchestrator.service.PolicyRepository;
import tss.orchestrator.service.SmartPolicyRepository;
import tss.orchestrator.service.UserRepository;
import tss.orchestrator.service.impl.BlockChainServiceImpl;
import tss.orchestrator.utils.constants.Constants;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;
import tss.orchestrator.utils.helpers.Validator;

import javax.swing.text.html.HTML;
import java.net.URI;
import java.time.Instant;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class UIRestController implements UIRestApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private SmartPolicyRepository smartPolicyRepository;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    private Validator validate = new Validator();


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
    public ResponseEntity<String> createPolicy(@PathVariable int userId, @RequestBody PolicyDTO policyDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) {
            Instant instant = Instant.now();
            if(policyDTO.getExpiryTimestamp()<= instant.getEpochSecond()){
                return new ResponseEntity<>("Wrong expiry timestamp",HttpStatus.BAD_REQUEST);
            }
            else if(!validate.cifValidator(policyDTO.getPolicyHolderCIF())){
                return new ResponseEntity<>("Incorrect format", HttpStatus.BAD_REQUEST);
            }
            ModelMapper modelMapper = new ModelMapper();
            Policy policy = modelMapper.map(policyDTO, Policy.class);
            policy.setUser(userOptional.get());

            policy.setInceptionTimestamp(instant.getEpochSecond());

            policyRepository.save(policy);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}").buildAndExpand(policy.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Object> createSmartPolicy(int userId, SmartPolicyDTO smartPolicyDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Policy> policyOptional = policyRepository.findById(smartPolicyDTO.getPolicyId());
        /*
        if(!validate.smartContractAddressValidator(userOptional.get().)){
            return new ResponseEntity<>("Wrong address format", HttpStatus.BAD_REQUEST);
        }


         */

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
        }
        smartPolicy.setId(null);

        //Blockchain interaction
        //blockChainService.initialize(userOptional.get().getPrivateKey());
        //BlockChainResponseTransfer responseTransfer = blockChainService.deployContract(smartPolicy);

        //Set other parameters
        policyRepository.setIsSmart(policyOptional.get().getId(), true);
        //smartPolicy.setContractAddress(responseTransfer.getContractAddress());
        //smartPolicy.setState(responseTransfer.getState());
        smartPolicy.setContractAddress("dsa6d1sa56d1sad684asd41as");
        smartPolicy.setState(Constants.ContractState.FUNDED);
        smartPolicyRepository.save(smartPolicy);

        //Response
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

    @Override
    public ResponseEntity<Object> deactivateSmartPolicy(int userId, int smartId) {
        Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartId);
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent() && smartPolicy.isPresent()){
            smartPolicy.get().setState(Constants.ContractState.DEACTIVATED);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
/*
    @Override
    public ResponseEntity<org.json.JSONObject> refreshAlerts(int userId, int smartId, String data) throws JSONException {
        Optional<User> user = userRepository.findById(userId);

        JSONObject json = new JSONObject(data);
        System.out.println(json);
        return new ResponseEntity<>(json,HttpStatus.OK);
    }

 */

    @Override
    public ResponseEntity<HashMap<String,Boolean>> refreshAlerts(int userId, int smartId, String data) throws JSONException {
        Optional<User> user = userRepository.findById(userId);
        JSONObject json = new JSONObject(data);

        HashMap<String,Boolean> map = new HashMap<>();

        //FALTA FER CONTROL DE ERRORS!!!!!!!!!

        if(user.isPresent()){
            JSONArray keys = json.names();
            for(int i=0;i<keys.length();i++){
                int smartPolicyId = keys.getInt(i);
                int alertId = json.getInt(keys.getString(i));
                Optional<SmartPolicy> smartPolicy = smartPolicyRepository.findById(smartPolicyId);
                if(smartPolicy.isPresent()) {
                    List<Alert> alerts = smartPolicy.get().getAlerts();
                    int j = alerts.size();
                    if (alerts.get(j - 1).getId() > alertId) {
                        map.put(keys.getString(i), Boolean.TRUE);
                    } else {
                        map.put(keys.getString(i), Boolean.FALSE);
                    }
                }else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }



}
