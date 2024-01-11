package it.rate.webapp.controllers;

import it.rate.webapp.models.*;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.RoleService;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/map")
public class MapController {
  private final InterestService interestService;
  private final UserService userService;
  private final RoleService roleService;
  private final PlaceService placeService;

  @GetMapping()
  public String mapView(Model model, @PathVariable Long interestId, Principal principal) {
    Optional<Interest> interest = interestService.findInterestById(interestId);
    if (interest.isEmpty()) {
      model.addAttribute("message", "This interest doesn't exist");
      return "error/page";
    }
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());

      model.addAttribute("loggedUser", loggedUser);
      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      optRole.ifPresent(role -> model.addAttribute("role", role.getRole()));
    }
    model.addAttribute("interest", interest.get());
    model.addAttribute("places", placeService.getPlaceInfoDTOS(interest.get()));
    return "interest/map";
  }
}
