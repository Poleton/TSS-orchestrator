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
    @SequenceGenerator(
            name = "alert_sequence",
            sequenceName= "alert_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "alert_sequence"
    )
    Integer alertId;

    Integer conditionValue;
    Integer conditionExceededValue;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmartPolicy smartPolicy;

}
