package tss.orchestrator.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tss.orchestrator.models.Policy;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {

}