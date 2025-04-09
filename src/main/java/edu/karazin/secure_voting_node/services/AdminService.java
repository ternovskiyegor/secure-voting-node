package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.dto.RegistrationAdminDto;
import edu.karazin.secure_voting_node.models.Admin;
import edu.karazin.secure_voting_node.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                admin.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Admin createNewAdmin(RegistrationAdminDto registrationAdminDto) {
        Admin admin = new Admin();
        admin.setEmail(registrationAdminDto.getEmail());
        admin.setPassword(passwordEncoder.encode(registrationAdminDto.getPassword()));
        admin.setRoles(List.of(roleService.getAdminRole()));
        return adminRepository.save(admin);
    }
}
