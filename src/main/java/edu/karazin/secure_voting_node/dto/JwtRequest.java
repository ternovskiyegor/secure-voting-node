package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
