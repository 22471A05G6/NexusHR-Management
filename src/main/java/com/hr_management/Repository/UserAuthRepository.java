package com.hr_management.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr_management.Entity.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);
}