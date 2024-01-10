package it.rate.webapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/map")
public class MapController {
  @GetMapping("/{interestId}")
  public String mapView(@PathVariable Long interestId) {
    return "interest/map";
  }
}
