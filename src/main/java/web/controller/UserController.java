package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public String openUserView(ModelMap model, Principal principal) {
		model.addAttribute("currentUser", us.findByUsername(principal.getName()));
		return "user";
	}
}