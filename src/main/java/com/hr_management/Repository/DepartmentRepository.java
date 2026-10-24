package com.hr_management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr_management.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
	boolean existsByName(String name);
}
