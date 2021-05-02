package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class SmartPolicyDTO {

    private int id;

    private String contractAddress;
    private String clientAddress;
    private String insuranceAddress;
    private String brokerAddress;


    private Timestamp activationTimestamp;
    private Timestamp expiryTimestamp;
    private Timestamp deactivationTimestamp;

    private Integer shipmentID;
    private Integer shipmentLiability;
    private List<Integer> sensorID;
    private List<Integer> sensorType;
    private Integer levelDepth;
    private Integer levelType;
    private Integer levelMinimumRange;
    private Integer levelMaximumRange;
    private Integer percentualWeight;

    private Integer sensorData;
    private Timestamp dataTimestamp;

    private Integer contractPremium;
    private Integer contractLiability;

}