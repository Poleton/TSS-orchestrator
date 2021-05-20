package tss.orchestrator.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tss.orchestrator.models.Policy;
import tss.orchestrator.utils.constants.Constants;

@Repository
@Transactional
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    @Modifying
    @Query("UPDATE Policy u SET u.smart = :smart WHERE u.id = :id")
    void smart(@Param(value = "id") Integer id, @Param(value = "smart") boolean smart);
}