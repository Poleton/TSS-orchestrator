package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PolicyDTO {

    private String policyName;
    private String policyHolderCIF;
    private String policyHolderName;
    private String product;
    private String territorialScope;
    private long contractPremium;
    private long contractLiability;
    private long expiryTimestamp;


}
