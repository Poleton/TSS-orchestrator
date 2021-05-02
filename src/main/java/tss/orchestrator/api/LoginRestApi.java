package tss.orchestrator.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.UserDTO;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.User;
import tss.orchestrator.utils.constants.Constants;


@RequestMapping(path=Constants.API_LOGIN)
public interface LoginRestApi {

    @PostMapping
    //ResponseEntity<Object> login(@RequestBody UserDTO user);
    ResponseEntity<Integer> login(@RequestBody UserDTO user);

}

