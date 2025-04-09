package edu.karazin.secure_voting_node.repositories;

import edu.karazin.secure_voting_node.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsAdminByEmail(String email);
}
