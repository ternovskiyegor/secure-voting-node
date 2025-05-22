package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.services.BallotService;
import edu.karazin.secure_voting_node.services.CryptoKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MobileClientController {

    private final BallotService ballotService;
    private final CryptoKeyService cryptoKeyService;

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        PublicKey publicKey = cryptoKeyService.getPublicKey();
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return ResponseEntity.ok(encodedKey);
    }

    @GetMapping("/results")
    public ResponseEntity<?> getResults() {
        return ResponseEntity.ok(ballotService.getResults());
    }

    @PostMapping("/vote")
    public ResponseEntity<?> receiveBallot(@RequestBody BallotRequest request) {
        ballotService.processBallot(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok("success");
    }
}
