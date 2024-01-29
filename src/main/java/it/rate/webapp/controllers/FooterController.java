package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class FooterController {

  private final UserService userService;

  @GetMapping("/contact")
  public String contact(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    return "footer/contact";
  }

  @GetMapping("/developers")
  public String developers(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    return "footer/developers";
  }

  @GetMapping("/about")
  public String about(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    return "footer/about";
  }

  @GetMapping("/terms")
  public String terms(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    return "footer/terms";
  }
}
