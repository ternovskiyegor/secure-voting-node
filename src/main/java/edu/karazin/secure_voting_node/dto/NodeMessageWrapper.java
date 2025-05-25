package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class NodeMessageWrapper {
    private NodeMessageType type;
    private BallotRequest ballot;
    private ConsensusMessage consensus;
}
