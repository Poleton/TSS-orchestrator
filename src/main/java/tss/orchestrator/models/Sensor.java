package tss.orchestrator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class Sensor {

    @Id
    @GeneratedValue
    private Integer id;

    private String type;
    private Integer levelDepth;
    private Integer levelMinimumRange;
    private Integer levelMaximumRange;
    private Integer percentualWeight;


}
