package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tss.orchestrator.api.LoginRestApi;
import tss.orchestrator.api.dto.PolicyDTO;
import tss.orchestrator.api.dto.UserDTO;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.User;
import tss.orchestrator.service.UserRepository;


import java.util.List;
import java.util.Optional;
@RestController
public class LoginController implements LoginRestApi {


    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Integer> login(UserDTO user) {

        User username = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
       // if(username == null) throw new UsernameNotFoundException(username);
        if(username != null) {
            //return ResponseEntity.ok().build();
            return new ResponseEntity<Integer>(username.getId(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //else return ResponseEntity.notFound().build();
                // cuando devuelva usuario correcto, devolver tambien el id del user para que puedean gestionar la url


    }
}