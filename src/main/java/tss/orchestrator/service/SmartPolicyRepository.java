package tss.orchestrator.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.constants.Constants;

@Repository
public interface SmartPolicyRepository extends JpaRepository<SmartPolicy, Integer> {
    @Modifying
    @Query("update SmartPolicy u set u.state = :state where u.id = :id")
    void setState(@Param(value = "id") long id, @Param(value = "state") Constants.ContractState state);

}
