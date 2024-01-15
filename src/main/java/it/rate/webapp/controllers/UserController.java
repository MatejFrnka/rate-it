package it.rate.webapp.controllers;

import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.dtos.SignupUserOutDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String signupPage() {
    return "user/signupForm";
  }

  @PostMapping("/signup")
  public String newUser(SignupUserInDTO userDTO, Model model) {
    try {
      userService.registerUser(userDTO);
    } catch (BadRequestException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("userDTO", new SignupUserOutDTO(userDTO));
      return "user/signupForm";
    }
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginPage(Principal principal) {
    if (principal != null) {
      return "redirect:/";
    }
    return "user/loginForm";
  }
}
