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
@RequestMapping("/interests/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;
  private final UserService userService;
  private final RatingService ratingService;
  private final PermissionService permissionService;

  @GetMapping("/new")
  @PreAuthorize("hasAnyAuthority(@permissionService.createPlace(#interestId))")
  public String newPlacePage(@PathVariable Long interestId, Model model, Principal principal) {
    model.addAttribute("place", new Place());
    model.addAttribute("method", "POST");
    model.addAttribute("action", "/interests/" + interestId + "/places/new");
    model.addAttribute("title", "New page");
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    return "place/form";
  }

  @PostMapping("/new")
  @PreAuthorize("hasAnyAuthority(@permissionService.createPlace(#interestId))")
  public String createNewPlace(@PathVariable Long interestId, @ModelAttribute Place place)
      throws BadRequestException {
    Place createdPlace = placeService.savePlace(place, interestId);
    return String.format("redirect:/interests/%d/places/%d", interestId, createdPlace.getId());
  }

  @GetMapping("/{placeId}")
  public String placeDetails(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal,
      HttpServletResponse response) {

    if (placeService.findById(placeId).isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      model.addAttribute("message", "This place doesn't exist");
      return "error/page";
    }

    Place place = placeService.getReferenceById(placeId);
    model.addAttribute("place", place);
    model.addAttribute("placeCriteria", placeService.getCriteriaOfPlaceDTO(place));

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
      if (permissionService.hasRatingPermission(loggedUser, place.getInterest())) {
        model.addAttribute("usersRatings", ratingService.getUsersRatingsDto(principal, placeId));
      }
    }
    return "place/page";
  }

  @PostMapping("/{placeId}/rate")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String ratePlace(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      @ModelAttribute RatingsDTO rating,
      Principal principal) {

    ratingService.updateRating(rating, placeId, principal);

    return String.format("redirect:/interests/%d/places/%d", interestId, placeId);
  }

  @GetMapping("/{placeId}/edit")
  @PreAuthorize("@permissionService.hasPlaceEditPermissions(#placeId, #interestId)")
  public String editPlacePage(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal) {

    model.addAttribute("method", "PUT");
    model.addAttribute("action", "/interests/" + interestId + "/places/" + placeId + "/edit");
    model.addAttribute("title", "Edit page");
    model.addAttribute("place", placeService.findById(placeId).get());
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }

    return "place/form";
  }

  @PutMapping("/{placeId}/edit")
  @PreAuthorize("@permissionService.hasPlaceEditPermissions(#placeId, #interestId)")
  public String editPlace(
          @PathVariable Long interestId,
          @PathVariable Long placeId,
          @ModelAttribute Place place)
          throws BadRequestException {

    if (!placeId.equals(place.getId())) {
      throw new BadRequestException("Invalid place id");
    }
    placeService.savePlace(place, interestId);
    return String.format("redirect:/interests/%d/places/%d", interestId, place.getId());
  }
}
