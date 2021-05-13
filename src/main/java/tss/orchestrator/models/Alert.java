package tss.orchestrator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tss.orchestrator.api.dto.AlertDTO;
import tss.orchestrator.utils.constants.Constants;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Map;

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
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Constants.ContractState state;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sensor_events_mapping",
            joinColumns = {@JoinColumn(name = "alert_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "sensor_events_id", referencedColumnName = "id")})
    @MapKey(name = "id")
    private Map<Constants.SensorType, SensorEvents> events;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SmartPolicy smartPolicy;

}
