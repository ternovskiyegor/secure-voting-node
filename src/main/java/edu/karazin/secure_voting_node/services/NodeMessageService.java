package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.dto.ConsensusMessage;
import edu.karazin.secure_voting_node.dto.NodeMessageType;
import edu.karazin.secure_voting_node.dto.NodeMessageWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NodeMessageService {

    private final BallotService ballotService;

    public void handleMessage(NodeMessageWrapper messageWrapper) {
        if (messageWrapper.getType() == NodeMessageType.BALLOT) {
            BallotRequest ballotRequest = messageWrapper.getBallot();
            log.info("Received ballot from another node: {}", ballotRequest.getId());
            ballotService.processBallot(ballotRequest);
        } else if (messageWrapper.getType() == NodeMessageType.CONSENSUS) {
            ConsensusMessage message = messageWrapper.getConsensus();
            log.info("Received consensus message from node: {}", message.getSenderNodeId());
            // TODO : consensus processing (after adding the algorithm)
        } else {
            log.warn("Unknown message type received: {}", messageWrapper.getType());
        }
    }
}
