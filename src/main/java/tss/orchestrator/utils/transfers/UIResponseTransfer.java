package tss.orchestrator.utils.transfers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import tss.orchestrator.models.Alert;
import tss.orchestrator.models.Policy;
import tss.orchestrator.models.SmartPolicy;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class UIResponseTransfer {
    private HttpStatus httpStatus;
    private String message;
    private List<Object> list;
    private boolean isError;
    private URI location;
    private HashMap<String,Integer> newAlerts;

    public UIResponseTransfer (){
        this.httpStatus = HttpStatus.OK;
        this.isError = false;
    }
}
