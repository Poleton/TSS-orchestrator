package tss.orchestrator.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SmartPolicy {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer smartContractId;
    private Integer policyNumber;
    private Integer blockchainId;
    private Integer contractModel;
    private String contractAddress;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Integer getContractModel() {
        return contractModel;
    }

    public void setContractModel(Integer contractModel) {
        this.contractModel = contractModel;
    }
}
