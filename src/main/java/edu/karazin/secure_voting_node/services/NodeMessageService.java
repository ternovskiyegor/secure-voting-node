package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.dto.ConsensusMessageDto;
import edu.karazin.secure_voting_node.models.Ballot;
import edu.karazin.secure_voting_node.repositories.BallotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NodeMessageService {

    private final BallotRepository ballotRepository;

    public void processBallotRequest(BallotRequest request) {
        if (ballotRepository.existsByVoterId(request.getId())) {
            log.info("Node ballot already exists from voterId={}", request.getId());
            return;
        }

        Ballot ballot = new Ballot();
        ballot.setVoterId(request.getId());
        ballot.setEncryptedVote(request.getVote());
        ballot.setSignature(request.getSignature());
        ballot.setCreatedAt(LocalDateTime.now());

        ballotRepository.save(ballot);
        log.info("Node ballot saved from voterId={}", request.getId());
    }

    public void processConsensusMessage(ConsensusMessageDto consensus) {
        // TODO: реалізувати конкретну логіку в залежності від типу повідомлення
        switch (consensus.getType()) {
            case "FINISH":
                // end voting
                break;
            case "SYNC":
                // initialize synchronization
                break;
            default:
                log.warn("Unknown consensus message type: {}", consensus.getType());
        }
    }
}
