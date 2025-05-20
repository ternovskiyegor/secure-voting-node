package edu.karazin.secure_voting_node.repositories;

import edu.karazin.secure_voting_node.models.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByIpAndPort(String ip, int port);
}
