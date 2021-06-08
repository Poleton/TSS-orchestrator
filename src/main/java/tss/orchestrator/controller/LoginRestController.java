package tss.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tss.orchestrator.api.LoginRestApi;
import tss.orchestrator.api.dto.UserDTO;
import tss.orchestrator.models.User;
import tss.orchestrator.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class LoginRestController implements LoginRestApi {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Integer> login(UserDTO user) {
        User username = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
        if(username != null) {
            return new ResponseEntity<>(username.getId(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}