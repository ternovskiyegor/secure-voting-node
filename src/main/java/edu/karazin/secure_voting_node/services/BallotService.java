package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.models.Ballot;
import edu.karazin.secure_voting_node.repositories.BallotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BallotService {

    private final BallotRepository ballotRepository;

    public void processBallot(BallotRequest request) {
        if (ballotRepository.existsByVoterId(request.getId())) {
            log.warn("Duplicate vote attempt by: {}", request.getId());
            return;
        }

        if (!isValid(request)) {
            log.warn("Invalid ballot format or signature");
            throw new IllegalArgumentException("Invalid ballot or signature");
        }

        Ballot ballot = new Ballot();
        ballot.setVoterId(request.getId());
        ballot.setEncryptedVote(request.getVote());
        ballot.setSignature(request.getSignature());
        ballot.setCreatedAt(LocalDateTime.now());

        ballotRepository.save(ballot);
        log.info("Ballot saved from voterId={}", request.getId());
    }

    private boolean isValid(BallotRequest request) {
        // TODO : Real checking of signature
        return request.getId() != null &&
                request.getVote() != null &&
                request.getSignature() != null;
    }

    public List<Ballot> getAllBallots() {
        return ballotRepository.findAll();
    }
}
