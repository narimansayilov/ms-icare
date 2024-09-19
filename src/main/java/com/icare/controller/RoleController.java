package com.icare.controller;

import com.icare.dto.request.RoleRequest;
import com.icare.dto.response.RoleResponse;
import com.icare.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addRole(@RequestBody @Valid RoleRequest request) {
        roleService.addRole(request);
    }

    @GetMapping("/{id}")
    public RoleResponse getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @GetMapping
    public List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/{id}")
    public RoleResponse updateRole(@PathVariable Long id,
                                   @RequestBody @Valid RoleRequest request) {
        return roleService.updateRole(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}
