package com.hr_management.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hr_management.ENUM.Permission;
import com.hr_management.Entity.UserAuth;
import com.hr_management.Repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository repo;

    public CustomUserDetailsService(UserAuthRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuth user = repo.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        Set<Permission> perms = RoleBasedPermissions.getRoleBasedPermissions().get(user.getRole());
        if (perms != null) {
            for (Permission p : perms) {
                authorities.add(new SimpleGrantedAuthority(p.name()));
            }
        }

        return new User(user.getUserEmail(), user.getPassword(), authorities);
    }
}