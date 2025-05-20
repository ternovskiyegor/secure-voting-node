package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.dto.ConsensusMessageDto;
import edu.karazin.secure_voting_node.services.NodeMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class NodeSyncController {

    private final NodeMessageService nodeMessageService;

    @PostMapping("/ballot")
    public ResponseEntity<?> receiveBallotFromNode(@RequestBody BallotRequest request) {
        nodeMessageService.processBallotRequest(request);
        return ResponseEntity.ok("Ballot from node accepted");
    }

    @PostMapping("/consensus")
    public ResponseEntity<?> receiveConsensusMessage(@RequestBody ConsensusMessageDto consensus) {
        nodeMessageService.processConsensusMessage(consensus);
        return ResponseEntity.ok("Consensus message processed");
    }
}
