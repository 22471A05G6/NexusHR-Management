package com.hr_management.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hr_management.DTO.EmployeeRequest;
import com.hr_management.DTO.EmployeeResponse;
import com.hr_management.DTO.EmployeeUpdateRequest;
import com.hr_management.DTO.PageResponse;
import com.hr_management.ENUM.EmployeeStatus;
import com.hr_management.ENUM.Role;
import com.hr_management.Entity.Department;
import com.hr_management.Entity.Employee;
import com.hr_management.Entity.UserAuth;
import com.hr_management.Repository.DepartmentRepository;
import com.hr_management.Repository.EmployeeRepository;
import com.hr_management.Repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final UserAuthRepository userRepo;
    private final PasswordEncoder encoder;

    public EmployeeResponse create(EmployeeRequest req, boolean callerIsAdmin) {
        if (userRepo.existsByUserEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        Role role = (req.role() == null) ? Role.EMPLOYEE : req.role();
        if (role != Role.EMPLOYEE && !callerIsAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only an admin can create HR or admin accounts");
        }

        UserAuth account = UserAuth.builder()
                .userName(req.firstName() + " " + req.lastName())
                .userEmail(req.email())
                .password(encoder.encode(req.password()))
                .role(role)
                .build();

        Employee employee = Employee.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .phone(req.phone())
                .designation(req.designation())
                .salary(req.salary())
                .joiningDate(req.joiningDate())
                .status(EmployeeStatus.ACTIVE)
                .department(getDepartment(req.departmentId()))
                .userAuth(account)
                .build();

        return toResponse(employeeRepo.save(employee));
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> search(String search, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        PageRequest pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by("id"));
        String q = (search == null) ? "" : search.trim();

        Page<Employee> result = employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUserAuth_UserEmailContainingIgnoreCase(
                        q, q, q, pageable);

        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return toResponse(getEmployee(id));
    }

    public EmployeeResponse update(Long id, EmployeeUpdateRequest req) {
        Employee e = getEmployee(id);
        e.setFirstName(req.firstName());
        e.setLastName(req.lastName());
        e.setPhone(req.phone());
        e.setDesignation(req.designation());
        e.setSalary(req.salary());
        e.setJoiningDate(req.joiningDate());
        e.setStatus(req.status());
        e.setDepartment(getDepartment(req.departmentId()));
        return toResponse(employeeRepo.save(e));
    }

    public void delete(Long id) {
        employeeRepo.delete(getEmployee(id));
    }

    private Employee getEmployee(Long id) {
        return employeeRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    private Department getDepartment(Long id) {
        return departmentRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    private EmployeeResponse toResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getUserAuth().getUserEmail(),
                e.getPhone(),
                e.getDesignation(),
                e.getSalary(),
                e.getJoiningDate(),
                e.getStatus().name(),
                e.getDepartment().getId(),
                e.getDepartment().getName(),
                e.getUserAuth().getRole().name());
    }
}