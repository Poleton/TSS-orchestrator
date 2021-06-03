package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter
public class SensorsDataDTO {
    private Integer userId;
    private Integer smartPolicyId;
    private Map<String, Long> sensorData;
    private long dataTimeStamp;
}
