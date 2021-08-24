package web.service;

import web.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getRoles();
    Set<Role> getRolesByName(String[] roles);
}
