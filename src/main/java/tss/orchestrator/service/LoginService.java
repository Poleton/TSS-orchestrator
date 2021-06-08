package tss.orchestrator.service;

import tss.orchestrator.api.dto.UserDTO;
import tss.orchestrator.utils.transfers.LoginResponseTransfer;

public interface LoginService {
    LoginResponseTransfer login(UserDTO user);
}
