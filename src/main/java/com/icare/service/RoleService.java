package com.icare.service;

import com.icare.dto.request.RoleRequest;
import com.icare.dto.response.RoleResponse;
import com.icare.entity.RoleEntity;
import com.icare.exception.NotFoundException;
import com.icare.mapper.RoleMapper;
import com.icare.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void addRole(RoleRequest request) {
        roleRepository.save(RoleMapper.INSTANCE.requestToEntity(request));
    }

    public RoleResponse getRole(Long id) {
        RoleEntity entity = roleRepository.findById(id).orElseThrow(() ->
                new NotFoundException("ROLE_NOT_FOUND"));
        return RoleMapper.INSTANCE.entityToResponse(entity);
    }

    public List<RoleResponse> getAllRoles() {
        return RoleMapper.INSTANCE.entitiesToResponses(roleRepository.findAll());
    }

    public RoleResponse updateRole(Long id, RoleRequest request) {
        RoleEntity entity = roleRepository.findById(id).orElseThrow(() ->
                new NotFoundException("ROLE_NOT_FOUND"));
        RoleMapper.INSTANCE.mapRequestToEntity(entity, request);
        roleRepository.save(entity);
        return RoleMapper.INSTANCE.entityToResponse(entity);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
