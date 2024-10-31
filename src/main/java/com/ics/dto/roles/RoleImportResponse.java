package com.ics.dto.roles;

import com.ics.dto.permissions.PermissionImportResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleImportResponse {
    public String rolePublicName;
    public String status;
    public List<PermissionImportResponse> permissions = new ArrayList<>();
}
