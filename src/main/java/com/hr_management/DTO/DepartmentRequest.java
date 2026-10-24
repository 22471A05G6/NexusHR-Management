package com.hr_management.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
		@NotBlank @Size(max = 100)String name,
		@Size(max=255)String description
		) {

}
