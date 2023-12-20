package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Place;
import it.rate.webapp.services.CriterionService;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.RatingService;
import it.rate.webapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;
  private final UserService userService;
  private final RatingService ratingService;
  private final CriterionService criterionService;

  @GetMapping("/new-place")
  public String newPlacePage(@PathVariable Long interestId, Model model) {

    model.addAttribute("place", new Place());
    model.addAttribute("method", "POST");
    model.addAttribute("action", "/" + interestId + "/places/new-place");
    model.addAttribute("title", "New page");

    return "placeForm";
  }

  @PostMapping("/new-place")
  public String createNewPlace(@PathVariable Long interestId, @ModelAttribute Place place)
      throws BadRequestException {

    Place createdPlace = placeService.saveNewPlace(place, interestId);

    return "redirect:/" + interestId + "/places/" + createdPlace.getId();
  }

  @GetMapping("/{placeId}")
  public String placeDetails(
      @PathVariable String interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal) {
    Optional<Place> optPlace = placeService.findById(placeId);
    if (optPlace.isEmpty()) {
      model.addAttribute("message", "This place doesn't exist");
      return "errorPage";
    }
    Place place = optPlace.get();
    model.addAttribute("place", place);
    model.addAttribute("placeCriteria", place.getInterest().getCriteria());
    if (principal != null) {
      AppUser loggedUser =
          userService
              .findByEmail(principal.getName())
              .orElseThrow(() -> new RuntimeException("Email not found in the database"));
      List<Criterion> loggedUserRatedCriteria =
          criterionService.findAllByInterestAppUserPlace(place.getInterest(), loggedUser, place);
      model.addAttribute("loggedUser", loggedUser);
      model.addAttribute("loggedUserRatedCriteria", loggedUserRatedCriteria);
      model.addAttribute("ratingService", ratingService);
    }
    return "place";
  }

  @PostMapping("/{placeId}")
  public String ratePlace(@PathVariable Long interestId, @PathVariable Long placeId) {
    // todo: accept updated list of ratings
    // todo: save new/updated ratings
    // todo: redirect to GET of place
    return "redirect:/places/{placeId}";
  }

  @GetMapping("/{placeId}/edit")
  public String editPlacePage(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal,
      HttpServletResponse response) throws BadRequestException {

    if (placeService.findById(placeId).isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      model.addAttribute("message", "This place doesn't exist");
    }

    if (!placeService.isCreator(principal.getName(), placeId)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "notAuthorized";
    }

    model.addAttribute("method", "PUT");
    model.addAttribute("action", "/" + interestId + "/places/" + placeId + "/edit");
    model.addAttribute("title", "Edit page");
    model.addAttribute("place", placeService.findById(placeId).get());

    return "placeForm";
  }

  @PutMapping("/{placeId}/edit")
  public String editPlace(@PathVariable Long interestId, @ModelAttribute Place place)
      throws BadRequestException {

    Place editedPlace = placeService.saveNewPlace(place, interestId);
    return "redirect:/" + interestId + "/places/" + editedPlace.getId();
  }
}
