package com.hr_management.Security;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hr_management.ENUM.Permission;
import com.hr_management.Entity.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    private static final long EXPIRE_MS = 1000L * 60 * 60 * 12; // 12 hours

    private final SecretKey key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("jwt.secret must be at least 32 characters");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserAuth user) {
        Set<Permission> perms = RoleBasedPermissions.getRoleBasedPermissions().get(user.getRole());
        List<String> permNames = (perms == null)
                ? List.of()
                : perms.stream().map(Enum::name).toList();

        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUserEmail())
                .claim("role", user.getRole().name())
                .claim("permissions", permNames)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRE_MS))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserEmail(String token) {
        return getClaims(token).getSubject();
    }
}