package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import java.security.Principal;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(("/about"))
public class FooterController {

  private final UserService userService;
  private final BuildProperties buildProperties;

  @GetMapping("/contact")
  public String contact(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
    return "footer/contact";
  }

  @GetMapping("/developers")
  public String developers(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
    return "footer/developers";
  }

  @GetMapping("/")
  public String about(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
    return "footer/about";
  }

  @GetMapping("/privacy-policy")
  public String privacy(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
    return "footer/legal/privacyPolicy";
  }

  @GetMapping("/terms-and-conditions")
  public String terms(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
    return "footer/legal/termsAndConditions";
  }
}
