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

}
