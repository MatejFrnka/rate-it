package it.rate.webapp.controllers;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidPlaceDetailsException;
import it.rate.webapp.exceptions.notfound.PlaceNotFoundException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import it.rate.webapp.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;
  private final UserService userService;
  private final RatingService ratingService;
  private final PermissionService permissionService;
  private final RoleService roleService;

  @GetMapping("/new")
  @PreAuthorize("@permissionService.createPlace(#interestId)")
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
  @PreAuthorize("@permissionService.createPlace(#interestId)")
  public String createNewPlace(@PathVariable Long interestId, @ModelAttribute Place place) {
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

    Place place = placeService.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    model.addAttribute("place", place);
    model.addAttribute("placeCriteria", placeService.getCriteriaOfPlaceDTO(place));

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
      if (permissionService.hasRatingPermission(loggedUser, place.getInterest())) {
        model.addAttribute("usersRatings", ratingService.getUsersRatingsDto(principal, placeId));
      }
      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      if (optRole.isPresent() && optRole.get().getRole().equals(Role.RoleType.APPLICANT)) {
        model.addAttribute("applicant", true);
      }
    }
    return "place/page";
  }

  @PostMapping("/{placeId}/rate")
  @PreAuthorize("@permissionService.ratePlace(#placeId)")
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
      @PathVariable Long interestId, @PathVariable Long placeId, Model model, Principal principal) {

    model.addAttribute("method", "PUT");
    model.addAttribute("action", "/interests/" + interestId + "/places/" + placeId + "/edit");
    model.addAttribute("title", "Edit page");
    model.addAttribute("place", placeService.getById(placeId));
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    return "place/form";
  }

  @PutMapping("/{placeId}/edit")
  @PreAuthorize("@permissionService.hasPlaceEditPermissions(#placeId, #interestId)")
  public String editPlace(
      @PathVariable Long interestId, @PathVariable Long placeId, @ModelAttribute Place place) {

    if (!placeId.equals(place.getId())) {
      throw new InvalidPlaceDetailsException("Invalid place id");
    }
    placeService.savePlace(place, interestId);
    return String.format("redirect:/interests/%d/places/%d", interestId, place.getId());
  }
}
