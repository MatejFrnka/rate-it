package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
  public String index(Model model, Optional<Principal> principal) {


    //todo: Get user using spring security and test after pull
    if(principal.isPresent()) {
      AppUser loggedUser = userService.findByEmail(principal.get().getName()).get(); //Principal without name cannot exist
      model.addAttribute("likedInterests", interestService.getLikedInterests(loggedUser));
    }

    return "index";
  }
}
