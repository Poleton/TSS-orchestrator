package tss.orchestrator.models;

import tss.orchestrator.api.dto.UserDTO;

import javax.persistence.*;
import java.util.List;

@Entity(name ="User") // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Policy> policies;

    @OneToMany(mappedBy = "user")
    private List<SmartPolicy> smartPolicies;

    //@OneToMany(mappedBy = "user")
    //private List<SmartPolicy> smartPolicies;
    public User(UserDTO user) {
        super();
        this.name = user.getName();
        this.password = user.getPassword();
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public List<SmartPolicy> getSmartPolicies() { return smartPolicies;}

    public void setSmartPolicies(List<SmartPolicy> smartPolicies) {this.smartPolicies = smartPolicies; };

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
