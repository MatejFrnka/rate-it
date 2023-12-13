package it.rate.webapp.controllers;

import it.rate.webapp.services.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainPageController {

  private final InterestService interestService;

  @GetMapping("/getAllSuggestions")
  public ResponseEntity<?> getAllSuggestions() {

    return ResponseEntity.ok().body(interestService.getAllSuggestionDtos());
  }

  @GetMapping({"/", "/index"})
  public String index(Model model, @RequestParam Optional<String> query) {

    if (query.isEmpty()) {
      model.addAttribute("interests", interestService.findAllInterests());
    } else {
      model.addAttribute("interests", interestService.findInterestsByName(query.get()));
    }

    return "index";
  }
}
