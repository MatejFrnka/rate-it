package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.services.PermissionService;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.RatingService;
import it.rate.webapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;
  private final UserService userService;
  private final RatingService ratingService;
  private final PermissionService permissionService;

  @GetMapping("/new-place")
  @PreAuthorize("hasAnyAuthority(@permissionService.createPlace(#interestId))")
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
    Place createdPlace = placeService.savePlace(place, interestId);
    return String.format("redirect:/%s/places/%s", interestId, createdPlace.getId());
  }

  @GetMapping("/{placeId}")
  public String placeDetails(
      @PathVariable String interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal,
      HttpServletResponse response) {

    if (placeService.findById(placeId).isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      model.addAttribute("message", "This place doesn't exist");
      return "errorPage";
    }

    Place place = placeService.getReferenceById(placeId);
    model.addAttribute("place", place);
    model.addAttribute("placeCriteria", placeService.getCriteriaOfPlaceDTO(place));

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser.getUsername());
      if (permissionService.hasRatingPermission(loggedUser, place.getInterest())) {
        model.addAttribute("usersRatings", ratingService.getUsersRatingsDto(principal, placeId));
      }
    }
    return "place";
  }

  @PostMapping("/{placeId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String ratePlace(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      @ModelAttribute RatingsDTO rating,
      Principal principal) {

    ratingService.updateRating(rating, placeId, principal);

    return String.format("redirect:/%s/places/%s", interestId, placeId);
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
      return "errorPage";
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
  public String editPlace(
      @PathVariable Long interestId,
      @ModelAttribute Place place,
      HttpServletResponse response,
      Principal principal) throws BadRequestException {
    if (placeService.findById(place.getId()).isEmpty()
        || !placeService.isCreator(principal.getName(), place.getId())) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "notAuthorized";
    }

    placeService.savePlace(place, interestId);

    return String.format("redirect:/%s/places/%s", interestId, place.getId());
  }
}
