package com.hr_management.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hr_management.DTO.DepartmentRequest;
import com.hr_management.DTO.DepartmentResponse;
import com.hr_management.Service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE')")
    public DepartmentResponse create(@Valid @RequestBody DepartmentRequest req) {
        return service.create(req);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_EMPLOYEE')")
    public List<DepartmentResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_EMPLOYEE')")
    public DepartmentResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
    public DepartmentResponse update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}