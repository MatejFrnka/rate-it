package it.rate.webapp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @GetMapping("/signup")
    public String signupPage() {

        return "signupForm";
    }

    @PostMapping("/signup")
    public String newUser() {
        //todo:RequestBody of object account
        //todo: Create logic to verify valid email, email being non existent, valid password, valid username
        //todo: return to page where user accessed signup form

        return "todo";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login() {
        //todo:RequestBody of object account
        //todo: find user by username(otherwise return bad username/password), validate password, redirect to main page
        //todo: return to page where user accessed signup form
        return "todo";
    }


}
