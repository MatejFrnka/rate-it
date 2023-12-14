package it.rate.webapp.controllers;

import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.dtos.SignupUserOutDTO;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String signupPage() {
    return "signupForm";
  }

  @PostMapping("/signup")
  public String newUser(SignupUserInDTO userDTO, Model model) {
    try {
      AppUser user = userService.registerUser(userDTO);
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("userDTO", new SignupUserOutDTO(userDTO));
      return "signupForm";
    }
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginPage() {
    return "loginForm";
  }

}
