package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tss.orchestrator.api.dto.PolicyDTO;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class Policy{

    @Id
    @GeneratedValue
    private Integer id;
    private String holderName;
    private String product;
    private String duration;
    private String territorialScope;
    private String meansOfTransport;
    private int numSensors;
    private String conditions;
    private long inceptionTimestamp;
    private long expiryTimestamp;



    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore
    private User user;



    public Policy(){

    }

    public Policy (PolicyDTO policyDTO, User user){
        //super();
        this.holderName = policyDTO.getHolderName();
        this.product= policyDTO.getProduct();
        this.duration = policyDTO.getDuration();
        this.conditions = policyDTO.getConditions();
        this.territorialScope = policyDTO.getTerritorialScope();
        this.meansOfTransport = policyDTO.getMeansOfTransport();
        this.numSensors = policyDTO.getNumSensors();
        this.user = user;
    }

    public long getInceptionTimestamp() {
        return inceptionTimestamp;
    }

    public void setInceptionTimestamp(long inceptionTimestamp) {
        this.inceptionTimestamp = inceptionTimestamp;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return holderName;
    }

    public void setName(String name) {
        this.holderName = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTerritorialScope() {
        return territorialScope;
    }

    public void setTerritorialScope(String territorialScope) {
        this.territorialScope = territorialScope;
    }

    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public void setMeansOfTransport(String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    public int getNumSensors() {
        return numSensors;
    }

    public void setNumSensors(int numSensors) {
        this.numSensors = numSensors;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", product='" + product + '\'' +
                ", duration='" + duration + '\'' +
                ", territorialScope='" + territorialScope + '\'' +
                ", meansOfTransport='" + meansOfTransport + '\'' +
                ", numSensors=" + numSensors +
                ", conditions='" + conditions + '\'' +
                ", user=" + user +
                '}';
    }
}
