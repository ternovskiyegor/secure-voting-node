package edu.karazin.secure_voting_node.dto;

import lombok.Data;

@Data
public class RegistrationAdminDto {
    private String email;
    private String password;
    private String confirmPassword;
}
