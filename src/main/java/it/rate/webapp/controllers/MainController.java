package it.rate.webapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  @GetMapping({"/", "/index"})
  public String index(Model model, String query) {
    // todo: load database and send to view
    // todo: if query null, load whole database ordered by rating. Otherwise load according to query
    // param
    return "index";
  }
}
