package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.dto.roles.CreateRoleDTO;
import com.example.usermanagement.dto.roles.RoleEditRequest;
import com.example.usermanagement.dto.roles.RoleEditResponse;
import com.example.usermanagement.entities.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IRoleService {
    Page<Role> searchAndSortRoles(String publicName, String sort, int page, int size, String direction);
    List<RoleEditResponse> editRoles(List<RoleEditRequest> requestBody);
    Role createRole(CreateRoleDTO requestBody);
    void deleteRole(UUID id);
    Role getRoleById(UUID id);
}
