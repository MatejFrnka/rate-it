package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;
  private final UserService userService;

  @GetMapping("/new-place")
  public String newPlacePage(@PathVariable Long interestId) {
    // todo: return view for new place creation
    return "placeForm";
  }

  @PostMapping("/new-place")
  public String createNewPlace(@PathVariable Long interestId) {
    // todo: RequestBody - place
    // todo: connect user that created new place with this place and Interest. Save to db.
    return "redirect:/places/{placeId}";
  }

  @GetMapping("/{placeId}")
  public String placeDetails(
          @PathVariable Long interestId, @PathVariable Long placeId, Model model, Principal principal) {
    Optional<Place> optPlace = placeService.findById(placeId);
    if (optPlace.isEmpty()) {
      model.addAttribute("message", "This place doesn't exist");
      return "errorPage";
    }
    Place place = optPlace.get();
    model.addAttribute("place", place);
    model.addAttribute("criteria", place.getInterest().getCriteria());
    if (principal != null) {
      AppUser loggedUser = userService
              .findByEmail(principal.getName())
              .orElseThrow(() -> new RuntimeException("Email not found in the database"));
      model.addAttribute("loggedUserRatings", loggedUser.getRatings());}
    return "place";
  }

  @PostMapping("/{placeId}")
  public String ratePlace(@PathVariable Long interestId, @PathVariable Long placeId) {
    // todo: accept updated list of ratings
    // todo: save new/updated ratings
    // todo: redirect to GET of place
    return "redirect:/places/{placeId}";
  }

  @GetMapping("/places/{placeId}/edit")
  public String editPlacePage(
      @PathVariable Long interestId, @PathVariable Long placeId, Model model) {
    // todo: return edit form
    return "placeForm";
  }

  @PutMapping("/places/{placeId}/edit")
  public String editPlace() {
    // todo: accept Place object, save(overwrite) with new values
    // todo: redirect to GET of edited place

    return "redirect:/places/{placeId}";
  }
}
