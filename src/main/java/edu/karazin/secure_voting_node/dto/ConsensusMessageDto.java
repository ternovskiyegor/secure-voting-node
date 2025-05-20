package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class ConsensusMessageDto {
    private String type;    // "FINISH", "SYNC", "ACK"
    private String payload; // json
}
