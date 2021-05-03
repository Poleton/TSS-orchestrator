package tss.orchestrator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.AlertDTO;

import javax.persistence.*;

@Entity
@Table(name="Alert")
@Getter @Setter @NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue
    Integer alertId;

    Integer conditionValue;
    Integer conditionExceededValue;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmartPolicy smartPolicy;

    public Alert(AlertDTO alertDTO, SmartPolicy smartPolicy) {
        this.conditionValue = alertDTO.getConditionValue();
        this.conditionExceededValue = alertDTO.getConditionExceededValue();
        this.smartPolicy = smartPolicy;
    }
}
