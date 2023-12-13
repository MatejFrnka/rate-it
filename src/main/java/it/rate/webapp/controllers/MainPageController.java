package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Controller
@RequiredArgsConstructor
public class MainPageController {

  private final InterestService interestService;
  private final UserService userService;

  @GetMapping("/getAllSuggestions")
  public ResponseEntity<?> getAllSuggestions() {

    return ResponseEntity.ok().body(interestService.getAllSuggestionDtos());
  }

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

    return "index";
  }
}
