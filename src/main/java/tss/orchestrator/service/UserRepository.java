package tss.orchestrator.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tss.orchestrator.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   User findByNameAndPassword(String name,String password);
}