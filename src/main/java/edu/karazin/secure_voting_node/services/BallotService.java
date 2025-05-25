package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.BallotRequest;
import edu.karazin.secure_voting_node.dto.NodeMessageType;
import edu.karazin.secure_voting_node.dto.NodeMessageWrapper;
import edu.karazin.secure_voting_node.dto.VoteResultDto;
import edu.karazin.secure_voting_node.models.Ballot;
import edu.karazin.secure_voting_node.repositories.BallotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BallotService {

    private final BallotRepository ballotRepository;
    private final CryptoKeyService cryptoKeyService;
    private final NodeConnectionService nodeConnectionService;

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

        NodeMessageWrapper messageWrapper = new NodeMessageWrapper();
        messageWrapper.setType(NodeMessageType.BALLOT);
        messageWrapper.setBallot(request);
        nodeConnectionService.broadcastToNode(messageWrapper);
    }

    private boolean isValid(BallotRequest request) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(request.getPublicKey());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            byte[] signatureBytes = Base64.getDecoder().decode(request.getSignature());

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(request.getVote().getBytes(StandardCharsets.UTF_8));

            return sig.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public VoteResultDto getResults() {
        Map<String, Long> ballots = ballotRepository.findAll().stream()
                .map(Ballot::getEncryptedVote)
                .filter(v -> !v.equals("invalid"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return new VoteResultDto(ballots);
    }
}
