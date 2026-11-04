package com.hr_management.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hr_management.DTO.EmployeeRequest;
import com.hr_management.DTO.EmployeeResponse;
import com.hr_management.DTO.EmployeeUpdateRequest;
import com.hr_management.DTO.PageResponse;
import com.hr_management.Service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE')")
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest req, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return service.create(req, isAdmin);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_EMPLOYEE')")
    public PageResponse<EmployeeResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.search(search, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_EMPLOYEE')")
    public EmployeeResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}