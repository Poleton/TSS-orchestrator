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
    @GeneratedValue
    private Integer id;

    private Integer type;
    private Integer levelDepth;
    private Integer levelMinimumRange;
    private Integer levelMaximumRange;
    private Integer percentualWeight;

}
