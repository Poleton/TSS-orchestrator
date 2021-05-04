package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.utils.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table
@Getter @Setter
public class SmartPolicy<user> {

    @Id
    @GeneratedValue
    private Integer id;

    //From Policy
    private long inceptionTimestamp; //Deploy
    private boolean isSmart;
    private String policyName;
    private String policyHolderCIF;
    private String policyHolderName;
    private String product;
    private String territorialScope; //Deploy
    private long contractPremium; //Deploy
    private long contractLiability; //Deploy
    private long expiryTimestamp; //Deploy
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    //From DTO
    private Integer policyId;
    //  Deploy
    private String contractAddress;
    private String clientAddress;
    private String insuranceAddress;
    private String brokerAddress;
    //  Add shipment
    private long shipmentID;
    private Integer shipmentLiability;
    //  Add sensors & Condition
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sensors_mapping",
            joinColumns = {@JoinColumn(name = "smart_policy_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "sensor_id", referencedColumnName = "id")})
    @MapKey(name = "id")
    private Map<String, Sensor> sensors;

    //Extra
    @Enumerated(EnumType.STRING)
    private Constants.ContractState state = Constants.ContractState.NONE;
    //  Activation
    private long activationTimestamp; //first sensor sending
    //  Deactivation
    private long deactivationTimestamp; //when client says

    @OneToMany(mappedBy = "smartPolicy")
    private List<Alert> alerts;

}