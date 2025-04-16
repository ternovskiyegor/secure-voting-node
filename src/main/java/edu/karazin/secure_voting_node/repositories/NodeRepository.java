package edu.karazin.secure_voting_node.repositories;

import edu.karazin.secure_voting_node.models.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByIpAndPort(String ip, int port);
}
