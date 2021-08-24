package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.RoleRepository;
import web.model.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImp implements RoleService{

    private final RoleRepository roleRep;

    @Autowired
    public RoleServiceImp(RoleRepository roleRep) {
        this.roleRep = roleRep;
    }

    @Override
    public List<Role> getRoles() {
        return roleRep.findAll();
    }

    @Override
    public Set<Role> getRolesByName(String[] roles) {
        return Arrays.stream(roles).map(roleRep::findByRoleName).collect(Collectors.toSet());
    }
}
