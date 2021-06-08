package tss.orchestrator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tss.orchestrator.api.dto.UserDTO;
import tss.orchestrator.models.User;
import tss.orchestrator.repositories.UserRepository;
import tss.orchestrator.service.LoginService;
import tss.orchestrator.utils.transfers.LoginResponseTransfer;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponseTransfer login(UserDTO user) {
        LoginResponseTransfer responseTransfer = new LoginResponseTransfer();

        User username = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
        if(username != null) {
            responseTransfer.setHttpStatus(HttpStatus.OK);
            responseTransfer.setUserId(username.getId());
        }else{
            responseTransfer.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }

        return responseTransfer;
    }
}
