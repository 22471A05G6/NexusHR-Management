package com.hr_management.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//a temporary controller. We’ll delete it once the real employee module exists.
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/employees")
    @PreAuthorize("hasAuthority('VIEW_EMPLOYEE')")
    public String viewEmployees() {
        return "You can view employees (HR/Admin only)";
    }

    @GetMapping("/leave")
    @PreAuthorize("hasAuthority('APPLY_LEAVE')")
    public String applyLeave() {
        return "You can apply for leave";
    }
    
}