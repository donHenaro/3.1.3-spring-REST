package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class AdminController {

//--------------------REST------------------------------------------
    @GetMapping("/index")
    public String getAllUsersREST() {
        return "/index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}