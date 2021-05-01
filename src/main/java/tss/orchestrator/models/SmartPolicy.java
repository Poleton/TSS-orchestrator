package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.SmartPolicyDTO;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table
@Getter @Setter @NoArgsConstructor
public class SmartPolicy<user> {

    @Id
    @GeneratedValue
    private Integer smartId;

    //POLICY
    private String holderName;
    private String territorialScope;
    private String meansOfTransport;
    private String product;
    private int numSensors;
    private String conditions;
    private Integer policyId;
    private Timestamp inception_timestamp;

    //SMART CONTRACT
    private String contractAddress;
    private String clientAddress;
    private String insuranceAddress;
    private String brokerAddress;


    private Timestamp activationTimestamp;
    private Timestamp expiryTimestamp;
    private Timestamp deactivationTimestamp;

    private Integer shipmentID;
    private Integer shipmentLiability;
    private Integer sensorID;
    private Integer sensorType;
    private Integer levelDepth;
    private Integer levelType;
    private Integer levelMinimumRange;
    private Integer levelMaximumRange;
    private Integer percentualWeight;


    private Integer contractPremium;
    private Integer contractLiability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;


    public SmartPolicy(SmartPolicyDTO smartPolicyDTO, Policy policy, User user) {
        //policy parameters

        this.policyId = policy.getId();

        this.holderName = policy.getHolderName();
        this.territorialScope = policy.getTerritorialScope();
        this.meansOfTransport = policy.getMeansOfTransport();
        this.numSensors = policy.getNumSensors();
        this.conditions = policy.getConditions();
        this.inception_timestamp = policy.getCreationDate();
        this.product = policy.getProduct();

        //SmartPolicy Parameters
        this.expiryTimestamp = null;
        this.deactivationTimestamp = null;
        this.activationTimestamp = null;

        this.shipmentID = smartPolicyDTO.getShipmentID();
        this.shipmentLiability = smartPolicyDTO.getShipmentLiability();
        this.sensorID = smartPolicyDTO.getSensorID();
        this.sensorType = smartPolicyDTO.getSensorType();
        this.levelDepth = smartPolicyDTO.getLevelDepth();
        this.levelType = smartPolicyDTO.getLevelType();
        this.levelMinimumRange = smartPolicyDTO.getLevelMinimumRange();
        this.levelMaximumRange = smartPolicyDTO.getLevelMaximumRange();
        this.percentualWeight = smartPolicyDTO.getPercentualWeight();


        this.contractPremium = smartPolicyDTO.getContractPremium(); //SC
        this.contractLiability = smartPolicyDTO.getContractLiability();
        this.insuranceAddress = smartPolicyDTO.getInsuranceAddress(); //SC
        this.clientAddress = smartPolicyDTO.getClientAddress(); //SC
        this.brokerAddress = smartPolicyDTO.getBrokerAddress();
        this.contractAddress = null;


        this.user = user;

    }
}