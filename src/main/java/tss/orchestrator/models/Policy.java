package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tss.orchestrator.api.dto.PolicyDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Policy {

    @Id
    @GeneratedValue
    private Integer id;
    private String state;
    private String holderName;
    private Date creationDate;
    //private String holderCIF;
    //private String product;
    //private Date expeditionDate;
    //private Date expirationDate;
    //private String vehicle;


    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Policy(){

    }

    public Policy (PolicyDTO policyDTO, User user){
        super();
        this.state = policyDTO.getState();
        this.holderName = policyDTO.getHolderName();
        this.user = user;
        this.creationDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", holderName='" + holderName + '\'' +
                ", user=" + user +
                '}';
    }
}
