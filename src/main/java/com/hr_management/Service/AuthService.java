package com.hr_management.Service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hr_management.DTO.AuthResponse;
import com.hr_management.DTO.LoginRequest;
import com.hr_management.DTO.RegisterRequest;
import com.hr_management.ENUM.Role;
import com.hr_management.Entity.UserAuth;
import com.hr_management.Repository.UserAuthRepository;
import com.hr_management.Security.JwtTokenUtil;

@Service
public class AuthService {

    private final UserAuthRepository repo;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwt;

    public AuthService(UserAuthRepository repo, PasswordEncoder encoder, JwtTokenUtil jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public void register(RegisterRequest req) {
        if (repo.existsByUserEmail(req.userEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        UserAuth user = new UserAuth();
        user.setUserName(req.userName());
        user.setUserEmail(req.userEmail());
        user.setPassword(encoder.encode(req.password()));
        user.setRole(Role.EMPLOYEE);
        repo.save(user);
    }

    public AuthResponse login(LoginRequest req) {
        UserAuth user = repo.findByUserEmail(req.userEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new AuthResponse(jwt.generateToken(user), user.getRole().name());
    }
}