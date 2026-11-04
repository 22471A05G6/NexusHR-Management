package com.hr_management.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		String phone,
		String designation,
		BigDecimal salary,
		LocalDate joiningDate,
		String status,
		Long departmentId,
		String departmentName,
		String role
		) {

}
