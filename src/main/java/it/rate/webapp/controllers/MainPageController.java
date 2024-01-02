package it.rate.webapp.controllers;

import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainPageController {

  private final InterestService interestService;
  private final UserService userService;

  @GetMapping({"/", "/index"})
  public String index(Model model, Principal principal) {

    if (principal != null) {
      model.addAttribute(
          "loggedUser",
          userService
              .findByEmail(principal.getName())
              .orElseThrow(() -> new RuntimeException("Email not found in the database")));
      model.addAttribute("likedInterests", interestService.getLikedInterests(principal.getName()));
    }

    return "main/index";
  }
}
