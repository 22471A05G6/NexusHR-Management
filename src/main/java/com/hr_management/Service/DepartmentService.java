package com.hr_management.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hr_management.DTO.DepartmentRequest;
import com.hr_management.DTO.DepartmentResponse;
import com.hr_management.Entity.Department;
import com.hr_management.Repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repo;

    public DepartmentResponse create(DepartmentRequest req) {
        if (repo.existsByName(req.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department already exists");
        }
        Department saved = repo.save(Department.builder()
                .name(req.name())
                .description(req.description())
                .build());
        return toResponse(saved);
    }

    public List<DepartmentResponse> findAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public DepartmentResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public DepartmentResponse update(Long id, DepartmentRequest req) {
        Department d = getOrThrow(id);
        if (!d.getName().equals(req.name()) && repo.existsByName(req.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already in use");
        }
        d.setName(req.name());
        d.setDescription(req.description());
        return toResponse(repo.save(d));
    }

    public void delete(Long id) {
        repo.delete(getOrThrow(id));
    }

    private Department getOrThrow(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    private DepartmentResponse toResponse(Department d) {
        return new DepartmentResponse(d.getId(), d.getName(), d.getDescription());
    }
}