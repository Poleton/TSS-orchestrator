package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.PolicyDTO;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Policy{

    @Id
    @GeneratedValue
    private Integer id;
    private String holderName;
    private String product;
    private String duration;
    private String territorialScope;
    private String meansOfTransport;
    private int numSensors;
    private String conditions;
    private long inceptionTimestamp;
    private long expiryTimestamp;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Policy (PolicyDTO policyDTO, User user){
        //super();
        this.holderName = policyDTO.getHolderName();
        this.product= policyDTO.getProduct();
        this.duration = policyDTO.getDuration();
        this.conditions = policyDTO.getConditions();
        this.territorialScope = policyDTO.getTerritorialScope();
        this.meansOfTransport = policyDTO.getMeansOfTransport();
        this.numSensors = policyDTO.getNumSensors();
        this.user = user;
    }

}
