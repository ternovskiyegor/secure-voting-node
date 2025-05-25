package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class ConsensusMessage {
    private String blockHash;
    private String senderNodeId;
    private String signature;
}
