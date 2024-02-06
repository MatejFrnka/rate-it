package it.rate.webapp.controllers;

import it.rate.webapp.dtos.PlaceInDTO;
import it.rate.webapp.dtos.ReviewDTO;
import it.rate.webapp.exceptions.notfound.PlaceNotFoundException;
import it.rate.webapp.models.*;
import it.rate.webapp.services.*;
import jakarta.transaction.Transactional;
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
  private final InterestService interestService;
  private final ReviewService reviewService;

  @GetMapping("/new")
  @PreAuthorize("@permissionService.createPlace(#interestId)")
  public String newPlacePage(@PathVariable Long interestId, Model model, Principal principal) {

    model.addAttribute("place", new Place());
    model.addAttribute("method", "POST");
    model.addAttribute("action", "/interests/" + interestId + "/places/new");
    model.addAttribute("title", "New page");
    model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));

    return "place/form";
  }

  @Transactional
  @PostMapping("/new")
  @PreAuthorize("@permissionService.createPlace(#interestId)")
  public String createNewPlace(
      @PathVariable Long interestId, PlaceInDTO placeDTO, Principal principal) {

    Interest interest = interestService.getById(interestId);
    AppUser loggedUser = userService.getByEmail(principal.getName());
    Place place = placeService.save(placeDTO, interest, loggedUser);

    return String.format("redirect:/interests/%d/places/%d", interestId, place.getId());
  }

  @GetMapping("/{placeId}")
  public String placeDetails(
      @PathVariable Long interestId, @PathVariable Long placeId, Model model, Principal principal) {

    Place place = placeService.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    model.addAttribute("place", place);
    model.addAttribute("placeCriteria", placeService.getCriteriaOfPlaceDTO(place));
    model.addAttribute("placeRatings", placeService.getPlaceUserRatingDto(place));

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
      if (permissionService.hasRatingPermission(loggedUser, place.getInterest())) {
        model.addAttribute("usersRatings", ratingService.getUsersRatingsDto(loggedUser, place));
        Optional<Review> optReview =
            reviewService.findById(new ReviewId(loggedUser.getId(), placeId));
        optReview.ifPresent(review -> model.addAttribute("review", new ReviewDTO(review)));
      }
      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      if (optRole.isPresent() && optRole.get().getRoleType().equals(Role.RoleType.APPLICANT)) {
        model.addAttribute("applicant", true);
      }
    }
    return "place/page";
  }

  @GetMapping("/{placeId}/edit")
  @PreAuthorize("@permissionService.hasPlaceEditPermissions(#placeId, #interestId)")
  public String editPlacePage(
      @PathVariable Long interestId, @PathVariable Long placeId, Model model, Principal principal) {

    model.addAttribute("method", "PUT");
    model.addAttribute("action", "/interests/" + interestId + "/places/" + placeId + "/edit");
    model.addAttribute("title", "Edit page");
    model.addAttribute("place", placeService.getById(placeId));
    model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));

    return "place/form";
  }

  @Transactional
  @PutMapping("/{placeId}/edit")
  @PreAuthorize("@permissionService.hasPlaceEditPermissions(#placeId, #interestId)")
  public String editPlace(
      @PathVariable Long interestId, @PathVariable Long placeId, PlaceInDTO placeDTO) {

    Place place = placeService.update(placeService.getById(placeId), placeDTO);

    return String.format("redirect:/interests/%d/places/%d", interestId, place.getId());
  }
}
