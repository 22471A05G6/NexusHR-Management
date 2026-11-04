package com.hr_management.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hr_management.ENUM.EmployeeStatus;

import jakarta.validation.constraints.*;

public record EmployeeUpdateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String phone,
        String designation,
        @NotNull @Positive BigDecimal salary,
        @NotNull LocalDate joiningDate,
        @NotNull Long departmentId,
        @NotNull EmployeeStatus status
) {}