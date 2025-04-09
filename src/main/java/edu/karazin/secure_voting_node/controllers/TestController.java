package edu.karazin.secure_voting_node.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/admin")
    public String getAdminData() {
        return "Admin Data";
    }

    @GetMapping("/super-admin")
    public String getSuperAdminData() {
        return "Super admin data";
    }

    @GetMapping("/info")
    public String getAdminInfo(Principal principal) {
        return principal.getName();
    }
}
