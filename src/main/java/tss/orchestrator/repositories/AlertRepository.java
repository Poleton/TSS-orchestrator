package tss.orchestrator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tss.orchestrator.models.Alert;
import tss.orchestrator.models.User;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

}