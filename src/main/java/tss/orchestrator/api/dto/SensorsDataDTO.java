package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;

@Getter @Setter
public class SensorsDataDTO {
    private Integer userId;
    private Integer smartPolicyId;
    private HashMap<Integer, Long> sensorData;
    private long dataTimeStamp;

}
