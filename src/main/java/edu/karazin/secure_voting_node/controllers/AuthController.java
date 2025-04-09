package edu.karazin.secure_voting_node.controllers;

import edu.karazin.secure_voting_node.dto.JwtRequest;
import edu.karazin.secure_voting_node.dto.RegistrationAdminDto;
import edu.karazin.secure_voting_node.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        return authService.createAuthToken(jwtRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewAdmin(@RequestBody RegistrationAdminDto registrationAdminDto) {
        return authService.createNewAdmin(registrationAdminDto);
    }
}
