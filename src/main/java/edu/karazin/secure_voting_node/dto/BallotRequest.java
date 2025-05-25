package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class BallotRequest {
    private String id;
    private String vote;
    private String signature;
    private String publicKey;
}
