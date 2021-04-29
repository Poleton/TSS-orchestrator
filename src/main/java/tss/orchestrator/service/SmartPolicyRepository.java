package tss.orchestrator.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tss.orchestrator.models.SmartPolicy;

@Repository
public interface SmartPolicyRepository extends JpaRepository<SmartPolicy, Integer> {
}
