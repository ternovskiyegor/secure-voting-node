package edu.karazin.secure_voting_node.services;

import edu.karazin.secure_voting_node.repositories.RoleRepository;
import edu.karazin.secure_voting_node.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN").get();
    }
}
