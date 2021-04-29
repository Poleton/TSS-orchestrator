package tss.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tss.orchestrator.api.dto.SmartPolicyDTO;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table
public class SmartPolicy<user> {

    @Id
    @GeneratedValue
    private Integer smartId;

    private String holderName;
    private String territorialScope;
    private String meansOfTransport;
    private String product;
    private int numSensors;
    private String conditions;
    private Integer policyId;
    private Date inception_timestamp;



    private int maximum_insured_sum;
    private int reserve;

    private Timestamp activation_timestamp;
    private Timestamp expiry_timestamp;
    private Timestamp deactivation_timestamp;



    private int processed_sensor_updates;
    private int sensor_updates_interval;
    private int premium_quantity;
    private int insurer_address;
    private int insured_address;



    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;





    public SmartPolicy(SmartPolicyDTO smartPolicyDTO,Policy policy,User user) {
        //policy parameters

       this.policyId = policy.getId();

        this.holderName = policy.getHolderName();
        this.territorialScope = policy.getTerritorialScope();
        this.meansOfTransport = policy.getMeansOfTransport();
        this.numSensors = policy.getNumSensors();
        this.conditions = policy.getConditions();
        this.inception_timestamp = policy.getCreationDate();
        this.product = policy.getProduct();

        //SmartPolicy Parameters
        this.maximum_insured_sum = smartPolicyDTO.getMaximum_insured_sum(); //sc
        this.reserve = smartPolicyDTO.getReserve(); //sc
        this.expiry_timestamp = null;
        this.deactivation_timestamp = null;
        this.activation_timestamp = null;

        this.processed_sensor_updates = smartPolicyDTO.getProcessed_sensor_updates(); //SC
        this.sensor_updates_interval = smartPolicyDTO.getSensor_updates_interval(); //SC
        this.premium_quantity = smartPolicyDTO.getPremium_quantity(); //SC
        this.insurer_address =smartPolicyDTO.getInsurer_address(); //SC
        this.insured_address = smartPolicyDTO.getInsured_address(); //SC


        this.user = user;

    }

    public SmartPolicy() {

    }


    public Integer getSmartId() {
        return smartId;
    }

    public void setSmartId(Integer smart_id) {
        this.smartId = smart_id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public int getMaximum_insured_sum() {
        return maximum_insured_sum;
    }

    public void setMaximum_insured_sum(int maximum_insured_sum) {
        this.maximum_insured_sum = maximum_insured_sum;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public int getProcessed_sensor_updates() {
        return processed_sensor_updates;
    }

    public void setProcessed_sensor_updates(int processed_sensor_updates) {
        this.processed_sensor_updates = processed_sensor_updates;
    }

    public int getSensor_updates_interval() {
        return sensor_updates_interval;
    }

    public void setSensor_updates_interval(int sensor_updates_interval) {
        this.sensor_updates_interval = sensor_updates_interval;
    }

    public int getPremium_quantity() {
        return premium_quantity;
    }

    public void setPremium_quantity(int premium_quantity) {
        this.premium_quantity = premium_quantity;
    }

    public int getInsurer_address() {
        return insurer_address;
    }

    public void setInsurer_address(int insurer_address) {
        this.insurer_address = insurer_address;
    }

    public int getInsured_address() {
        return insured_address;
    }

    public void setInsured_address(int insured_address) {
        this.insured_address = insured_address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
