package tss.orchestrator.utils.transfers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter @NoArgsConstructor
public class LoginResponseTransfer {
    private Integer userId;
    private HttpStatus httpStatus;
}
