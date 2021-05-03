package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class UserDTO {
    private String name;
    private String password;
    private String privateKey;
}
