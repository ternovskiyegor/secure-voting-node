package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.AdminDto;
import edu.karazin.secure_voting_node.dto.JwtRequest;
import edu.karazin.secure_voting_node.dto.JwtResponse;
import edu.karazin.secure_voting_node.dto.RegistrationAdminDto;
import edu.karazin.secure_voting_node.exceptions.AppError;
import edu.karazin.secure_voting_node.models.Admin;
import edu.karazin.secure_voting_node.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminService adminService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = adminService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewAdmin(@RequestBody RegistrationAdminDto registrationAdminDto) {
        if (!registrationAdminDto.getPassword().equals(registrationAdminDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Password do not matches"), HttpStatus.BAD_REQUEST);
        }
        if (adminService.findByEmail(registrationAdminDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "This user already exists"), HttpStatus.BAD_REQUEST);
        }
        Admin admin = adminService.createNewAdmin(registrationAdminDto);
        return ResponseEntity.ok(new AdminDto(admin.getId(), admin.getEmail()));
    }
}
