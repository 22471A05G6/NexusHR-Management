package com.hr_management.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr_management.ENUM.Role;
import com.hr_management.Entity.UserAuth;
import com.hr_management.Repository.UserAuthRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserAuthRepository repo;
    private final PasswordEncoder encoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminSeeder(UserAuthRepository repo,
                       PasswordEncoder encoder,
                       @Value("${app.admin.email}") String adminEmail,
                       @Value("${app.admin.password}") String adminPassword) {
        this.repo = repo;
        this.encoder = encoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (repo.existsByUserEmail(adminEmail)) {
            return;
        }
        UserAuth admin = UserAuth.builder()
                .userName("Admin")
                .userEmail(adminEmail)
                .password(encoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();
        repo.save(admin);
    }
}