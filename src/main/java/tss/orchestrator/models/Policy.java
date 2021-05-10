package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.PolicyDTO;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Policy{

    @Id
    @SequenceGenerator(
            name = "policy_sequence",
            sequenceName= "policy_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "policy_sequence"
    )
    private Integer id;
    private long inceptionTimestamp;
    private boolean isSmart;

    //From DTO
    private String policyName;
    private String policyHolderCIF;
    private String policyHolderName;
    private String product;
    private String territorialScope;
    private long contractPremium;
    private long contractLiability;
    private long expiryTimestamp;


    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore
    private User user;

}
