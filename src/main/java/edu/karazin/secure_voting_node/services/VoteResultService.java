package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.VoteResultDto;
import edu.karazin.secure_voting_node.models.Ballot;
import edu.karazin.secure_voting_node.repositories.BallotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteResultService {

    private final BallotRepository ballotRepository;

    public VoteResultDto calculateResults() {
        List<Ballot> ballots = ballotRepository.findAll();
        Map<String, Integer> results = new HashMap<>();

        for (Ballot ballot : ballots) {
            String encryptedVote = ballot.getEncryptedVote();
            results.put(encryptedVote, results.getOrDefault(encryptedVote, 0) + 1);
        }

        return new VoteResultDto(results);
    }

}
