package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PolicyDTO {

    private String holderName;
    private String product;
    private String duration;
    private String territorialScope;
    private String meansOfTransport;
    private int numSensors;
    private String conditions;

}
