package tss.orchestrator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tss.orchestrator.api.dto.UserDTO;

import javax.persistence.*;
import java.util.List;

@Entity(name ="User") // This tells Hibernate to make a table out of this class
@Table(name = "user")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName= "user_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer id;

    private String name;

    private String password;

    private String privateKey;

    @OneToMany(mappedBy = "user")
    private List<Policy> policies;

    @OneToMany(mappedBy = "user")
    private List<SmartPolicy> smartPolicies;

}
