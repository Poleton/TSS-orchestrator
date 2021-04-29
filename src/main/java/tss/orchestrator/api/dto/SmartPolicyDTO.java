package tss.orchestrator.api.dto;

import java.util.Date;

public class SmartPolicyDTO {
    private int maximum_insured_sum;
    private int reserve;
    private int id;






    //private Date inception_timestamp;
    //private Date expiry_timestamp;
    //private Date deactivation_timestamp;

    private int processed_sensor_updates;
    private int sensor_updates_interval;
    private int premium_quantity;
    private int insurer_address;
    private int insured_address;




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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /*
    public Date getInception_timestamp() {
        return inception_timestamp;
    }

    public void setInception_timestamp(Date inception_timestamp) {
        this.inception_timestamp = inception_timestamp;
    }

    public Date getExpiry_timestamp() {
        return expiry_timestamp;
    }

    public void setExpiry_timestamp(Date expiry_timestamp) {
        this.expiry_timestamp = expiry_timestamp;
    }

    public Date getDeactivation_timestamp() {
        return deactivation_timestamp;
    }

    public void setDeactivation_timestamp(Date deactivation_timestamp) {
        this.deactivation_timestamp = deactivation_timestamp;
    }

     */


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


}
