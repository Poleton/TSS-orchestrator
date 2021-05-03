package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.PolicyDTO;

import javax.persistence.*;
import java.sql.Timestamp;


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
    private Timestamp inceptionTimestamp;




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
        this.inceptionTimestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", product='" + product + '\'' +
                ", duration='" + duration + '\'' +
                ", territorialScope='" + territorialScope + '\'' +
                ", meansOfTransport='" + meansOfTransport + '\'' +
                ", numSensors=" + numSensors +
                ", conditions='" + conditions + '\'' +
                ", user=" + user +
                '}';
    }
}
