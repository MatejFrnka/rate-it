package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
import it.rate.webapp.models.*;
import it.rate.webapp.services.*;
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
public class MapController extends BaseThymeleafController{
  private final InterestService interestService;
  private final UserService userService;
  private final RoleService roleService;
  private final PlaceService placeService;
  private final LikeService likeService;

  @GetMapping()
  public String mapView(Model model, @PathVariable Long interestId, Principal principal) {

    Interest interest =
        interestService.findById(interestId).orElseThrow(InterestNotFoundException::new);
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      optRole.ifPresent(role -> model.addAttribute("role", role.getRoleType()));
      model.addAttribute(
          "liked", likeService.existsById(new LikeId(loggedUser.getId(), interestId)));
    }
    model.addAttribute("interest", interest);
    model.addAttribute("places", placeService.getPlaceInfoDTOS(interest));

    return "interest/map";
  }
}
