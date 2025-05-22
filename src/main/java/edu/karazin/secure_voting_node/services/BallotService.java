package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.dto.VoteResultDto;
import edu.karazin.secure_voting_node.models.Ballot;
import edu.karazin.secure_voting_node.repositories.BallotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BallotService {

    private final BallotRepository ballotRepository;
    private final CryptoKeyService cryptoKeyService;

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
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(cryptoKeyService.getPublicKey());
            signature.update(request.getVote().getBytes(StandardCharsets.UTF_8));
            byte [] sigBytes = Base64.getDecoder().decode(request.getSignature());
            return signature.verify(sigBytes);
        } catch (Exception e) {
            log.error("Signature verification failed", e);
            return false;
        }
    }

    public VoteResultDto getResults() {
        Map<String, Long> ballots = ballotRepository.findAll().stream()
                .map(ballot -> {
                    try {
                        return cryptoKeyService.decrypt(ballot.getEncryptedVote());
                    } catch (Exception e) {
                        log.error("Failed to decrypt vote from: {}", ballot.getVoterId(), e);
                        return "invalid";
                    }
                })
                .filter(v -> !v.equals("invalid"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return new VoteResultDto(ballots);
    }
}
