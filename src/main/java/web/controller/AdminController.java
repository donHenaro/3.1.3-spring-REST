package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

@Controller
@RequestMapping("")
public class AdminController {
    private final UserService us;
    private final RoleService rs;

    @Autowired
    public AdminController(UserService us, RoleService rs) {
        this.us = us;
        this.rs = rs;
    }

    @GetMapping("/admin/admin")
    public String getAllUsers(@ModelAttribute("user") User user, ModelMap model) {
        model.addAttribute("users", us.listUsers());
        model.addAttribute("currentUser",
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("roles", rs.getRoles());
        return "admin/admin";
    }

    @PostMapping("/create")
    public String createUserPost(@ModelAttribute("user") User user,
                                 @RequestParam(required = false, name = "listRoles") String[] arrRoles) {
        if (arrRoles != null) {
            user.setRoles(rs.getRolesByName(arrRoles));
        }
        us.create(user);
        return "redirect:/admin/admin";
    }

    //------Модальные диалоги--------

    @GetMapping(value = "/delete-user/{id}")
    public String deleteUserConfirmation(@PathVariable("id") long id, ModelMap model) {
        User user = us.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", rs.getRoles());
        return "admin/delete-user :: delete-user";
    }

    @PostMapping(value = "/delete-user/{id}")
    public String deleteUsert(@PathVariable("id") long id, ModelMap model) {
        us.deleteById(id);
        return "redirect:/admin/admin";
    }

    @GetMapping(value = "/edit-user/{id}")
    public String editUserModal(@PathVariable("id") long id, ModelMap model) {
        User user = us.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", rs.getRoles());
        return "admin/edit-user :: edit-user";
    }

    @PostMapping(value = "/edit-user")
    public String editUser(@ModelAttribute User user,
                           @RequestParam(required = false, name = "listRoles") String[] arrRoles) {
        if (arrRoles != null) {
            user.setRoles(rs.getRolesByName(arrRoles));
        }
        us.update(user);
        return "redirect:/admin/admin";
    }
}