package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;


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
    public String getAllUsers(ModelMap model) {
        model.addAttribute("users", us.listUsers());
        return "admin/admin";
    }

    @GetMapping("/user/{id}")
    public String openUserView(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", us.findById(id));
        model.addAttribute("info", "Кабинет администратора");
        return "user";
    }

    @GetMapping("/admin/create")
    public String createUserGet(@ModelAttribute("user") User user, ModelMap model) {
        model.addAttribute("roles", rs.getRoles());
        return "admin/create";
    }

    @PostMapping("/admin/create")
    public String createUserPost(@ModelAttribute("user") User user,
                                 @RequestParam(required = false, name = "listRoles") String[] arrRoles) {
        if(arrRoles != null){
            user.setRoles(rs.getRolesByName(arrRoles));
        }
        us.create(user);
        return "redirect:/admin/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        us.deleteById(id);
        return "redirect:/admin/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUserGet(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", us.findById(id));
        model.addAttribute("roles", rs.getRoles());
        return "update";
    }

    @PostMapping("/update")
    public String updateUser(User user,
                             @RequestParam(required = false, name = "listRoles") String[] arrRoles) {
        if(arrRoles != null){
            user.setRoles(rs.getRolesByName(arrRoles));
        }
        us.update(user);
        return "redirect:/admin/admin";
    }
}