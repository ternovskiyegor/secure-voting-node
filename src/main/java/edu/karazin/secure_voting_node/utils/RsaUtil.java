package edu.karazin.secure_voting_node.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    public static void main(String[] args) throws Exception {
        // 1. Генеруємо пару ключів RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 2. Створюємо vote
        String vote = "Candidate_B";

        // 3. Підписуємо vote приватним ключем
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(vote.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();

        // 4. Кодуємо все в Base64
        String voteEncoded = vote;
        String signatureEncoded = Base64.getEncoder().encodeToString(signatureBytes);
        String publicKeyEncoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // 5. Готуємо JSON
        Map<String, String> ballot = new HashMap<>();
        ballot.put("vote", voteEncoded);
        ballot.put("signature", signatureEncoded);
        ballot.put("publicKey", publicKeyEncoded);

        // 6. Виводимо JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ballot);
        System.out.println(json);
    }

}
