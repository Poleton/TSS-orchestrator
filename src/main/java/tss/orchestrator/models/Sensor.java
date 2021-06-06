package tss.orchestrator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.utils.constants.Constants;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Sensor {

    @Id
    @SequenceGenerator(
            name = "sensor_sequence",
            sequenceName= "sensor_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_sequence"
    )
    private Integer id;

    private String type;
    private Integer levelDepth;
    private Integer levelMinimumRange;
    private Integer levelMaximumRange;
    private Integer percentualWeight;

    //Extra
    private Integer contractContextId;

}
