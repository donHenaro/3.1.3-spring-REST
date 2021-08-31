package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("")
public class UserController {

	private final UserService us;

	@Autowired
	public UserController(UserService us) {
		this.us = us;
	}

	@GetMapping("/user")
	public String openUserView(ModelMap model) {
//		model.addAttribute("currentUser", us.findByUsername(principal.getName()));
		model.addAttribute("currentUser",
				(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return "user";
	}
}