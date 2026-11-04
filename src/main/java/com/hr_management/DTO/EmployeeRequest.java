package com.hr_management.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hr_management.ENUM.Role;

import jakarta.validation.constraints.*;

public record EmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        String phone,
        String designation,
        @NotNull @Positive BigDecimal salary,
        @NotNull LocalDate joiningDate,
        @NotNull Long departmentId,
        Role role
) {}