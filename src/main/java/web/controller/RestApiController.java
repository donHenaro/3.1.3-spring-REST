package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {
    private final UserService us;
    private final RoleService rs;

    @Autowired
    public RestApiController(UserService us, RoleService rs) {
        this.us = us;
        this.rs = rs;
    }

    //++++LIST_ALL+++++
    @GetMapping("/users")
    public ResponseEntity<List<User>> readAll() {
        final List<User> users = us.listUsers();
        return (users != null)
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //++++GET+++++
    @GetMapping("/users/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") long id) {
        final User user = us.findById(id);
        return (user != null)
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //++++CREATE+++++
    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        user.setRoles(rs.getRolesByName(user.getRolesStrArr()));
        us.create(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //++++UPDATE+++++
    @PutMapping("/users")
    public ResponseEntity<?> update(@RequestBody User user) {
        user.setRoles(rs.getRolesByName(user.getRolesStrArr()));
        us.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //++++DELETE+++++
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        us.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //++++Current user+++++
    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser() {
        User currUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(currUser, HttpStatus.OK);
    }

    //++++List Roles+++++
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> readAllRoles() {
        final List<Role> roles = rs.getRoles();
        return  new ResponseEntity<>(roles, HttpStatus.OK);
    }
}