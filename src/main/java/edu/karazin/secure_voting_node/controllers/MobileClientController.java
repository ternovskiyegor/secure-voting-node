package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.services.BallotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MobileClientController {

    private final BallotService ballotService;

    @PostMapping("/vote")
    public ResponseEntity<?> receiveBallot(@RequestBody BallotRequest request) {
        ballotService.processBallot(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/results")
    public ResponseEntity<?> getResults() {
        return ResponseEntity.ok(ballotService.getAllBallots());
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok("success");
    }
}
