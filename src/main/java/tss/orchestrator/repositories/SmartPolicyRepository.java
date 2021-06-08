package tss.orchestrator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tss.orchestrator.models.SmartPolicy;
import tss.orchestrator.utils.constants.Constants;

@Repository
@Transactional
public interface SmartPolicyRepository extends JpaRepository<SmartPolicy, Integer> {
    @Modifying
    @Query("UPDATE SmartPolicy u SET u.state = :state WHERE u.id = :id")
    void setState(@Param(value = "id") Integer id, @Param(value = "state") Constants.ContractState state);

    @Modifying
    @Query("UPDATE SmartPolicy u SET u.activationTimestamp = :activationTimestamp WHERE u.id = :id")
    void setActivationTimestamp(@Param(value = "id") Integer id, @Param(value = "activationTimestamp") long activationTimestamp);

    @Modifying
    @Query("UPDATE SmartPolicy u SET u.deactivationTimestamp = :deactivationTimestamp WHERE u.id = :id")
    void setDeactivationTimestamp(@Param(value = "id") Integer id, @Param(value = "deactivationTimestamp") long deactivationTimestamp);

}
