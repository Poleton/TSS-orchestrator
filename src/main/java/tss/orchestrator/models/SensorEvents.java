package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table
@Getter
@Setter
public class SensorEvents {
    @Id
    @SequenceGenerator(
            name = "sensor_events_sequence",
            sequenceName= "sensor_events_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_events_sequence"
    )
    @JsonIgnore
    private Integer id;

    private String type;
    private BigInteger levelID;
    private BigInteger updatedData;
    private BigInteger updatedDataExcess;
    private BigInteger levelExcessTime;
    private BigInteger contractReserve;
}
