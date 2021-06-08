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
import tss.orchestrator.repositories.AlertRepository;
import tss.orchestrator.repositories.SmartPolicyRepository;
import tss.orchestrator.repositories.UserRepository;
import tss.orchestrator.service.BlockChainService;
import tss.orchestrator.service.LoginService;
import tss.orchestrator.service.UIService;
import tss.orchestrator.utils.transfers.BlockChainResponseTransfer;

import java.util.Optional;

@RestController
public class BlockChainRestController implements BlockChainRestApi {

    @Autowired
    private BlockChainService blockChainServiceImpl;

    @Override
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO){

        blockChainServiceImpl.updateSensorsData(sensorsDataDTO);

        return ResponseEntity.accepted().build();
    }

}
