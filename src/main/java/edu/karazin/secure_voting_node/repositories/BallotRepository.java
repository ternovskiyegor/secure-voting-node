package edu.karazin.secure_voting_node.repositories;

import edu.karazin.secure_voting_node.models.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallotRepository extends JpaRepository<Ballot, Long> {
    boolean existsByVoterId(String voterId);
}
