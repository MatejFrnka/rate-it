package it.rate.webapp.controllers;

import it.rate.webapp.dtos.InterestSuggestionDto;
import it.rate.webapp.services.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final MainService mainService;

  @GetMapping("/getAllSuggestions")
  public ResponseEntity<?> getAllSuggestions() {

    return ResponseEntity.ok().body(mainService.getAllSuggestionDtos());

  }

  @GetMapping({"/", "/index"})
  public String index(Model model, String query) {

    if(query == null) {
      model.addAttribute("interests", mainService.findAllInterests());
    } else {
      model.addAttribute("interests", mainService.findInterestsByName(query));
    }

    return "index";
  }
}
