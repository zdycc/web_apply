package com.webb.service;

import com.webb.model.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles() throws RoleServiceException;
    Role getRoleById(int roleId) throws RoleServiceException;
    Role addRole(Role role) throws RoleServiceException;
    Role updateRole(Role role) throws RoleServiceException;
    void deleteRole(int roleId) throws RoleServiceException;
}

