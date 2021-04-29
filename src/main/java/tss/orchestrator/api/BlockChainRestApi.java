package tss.orchestrator.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.SensorsDataDTO;
import tss.orchestrator.utils.constants.Constants;

@RequestMapping(Constants.API_BLOCKCHAIN)
public interface BlockChainRestApi {

    @PostMapping(Constants.API_SENSORS)
    public ResponseEntity<Object> sendSensorsData(@RequestBody SensorsDataDTO sensorsDataDTO);

}
