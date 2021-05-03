package tss.orchestrator.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlertDTO {
    Integer conditionValue;
    Integer conditionExceededValue;
}
