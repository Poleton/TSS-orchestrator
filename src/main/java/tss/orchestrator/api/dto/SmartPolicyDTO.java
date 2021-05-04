package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;
import tss.orchestrator.models.Sensor;

import java.util.Map;

@Getter @Setter
public class SmartPolicyDTO {

    private Integer policyId;
    //  Deploy
    private String contractAddress;
    private String clientAddress;
    private String insuranceAddress;
    private String brokerAddress;
    //  Add shipment
    private long shipmentID;
    private Integer shipmentLiability;

    private Map<String, Sensor> sensors;
}