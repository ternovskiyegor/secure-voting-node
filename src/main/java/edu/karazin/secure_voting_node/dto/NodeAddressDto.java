package edu.karazin.secure_voting_node.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeAddressDto {
    private String ip;
    private int port;
}
