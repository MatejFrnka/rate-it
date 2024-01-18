package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
import it.rate.webapp.models.*;
import it.rate.webapp.services.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController {

  private final InterestService interestService;
  private final UserService userService;
  private final RoleService roleService;
  private final PermissionService permissionService;
  private final PlaceService placeService;
  private final LikeService likeService;

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @GetMapping("/create")
  public String createPage(Model model, Principal principal) {
    List<Criterion> criteria = new ArrayList<>();
    model.addAttribute("criteria", criteria);
    model.addAttribute("interest", new Interest());
    model.addAttribute("action", "/interests/create");
    model.addAttribute("method", "post");
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    return "interest/form";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/create")
  public String createNew(
      @ModelAttribute Interest interest,
      @RequestParam List<String> criteriaNames,
      RedirectAttributes ra,
      Principal principal) {
    Interest savedInterest = interestService.save(interest, criteriaNames);
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      likeService.createLike(loggedUser, savedInterest);
    }

    ra.addAttribute("id", savedInterest.getId());
    return "redirect:/interests/{id}";
  }

  @GetMapping("/{interestId}")
  public String interestView(Model model, @PathVariable Long interestId, Principal principal) {
    Interest interest =
        interestService.findById(interestId).orElseThrow(InterestNotFoundException::new);

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());

      model.addAttribute("loggedUser", loggedUser);
      model.addAttribute(
          "liked", likeService.existsById(new LikeId(loggedUser.getId(), interestId)));
      model.addAttribute(
          "ratingPermission", permissionService.hasRatingPermission(loggedUser, interest));

      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      optRole.ifPresent(role -> model.addAttribute("role", role.getRole()));
    }
    model.addAttribute("interest", interest);
    model.addAttribute("places", placeService.getPlaceInfoDTOS(interest));
    return "interest/page";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/{interestId}/request")
  public String applyForVoterAuthority(@PathVariable Long interestId, Principal principal) {
    Interest interest =
        interestService.findById(interestId).orElseThrow(InvalidInterestDetailsException::new);
    AppUser loggedUser = userService.getByEmail(principal.getName());
    roleService.setRole(interest, loggedUser, Role.RoleType.APPLICANT);
    return "redirect:/interests/{interestId}";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @GetMapping("/my")
  public String myInterests(Model model, Principal principal) {
    if (principal != null) {
      model.addAttribute(
          "loggedUser",
          userService
              .findByEmail(principal.getName())
              .orElseThrow(InvalidUserDetailsException::new));
      model.addAttribute(
          "likedInterests", interestService.getLikedInterestsDTOS(principal.getName()));
    }
    return "interest/seeAll";
  }
}
