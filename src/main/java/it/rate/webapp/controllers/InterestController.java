package it.rate.webapp.controllers;

import it.rate.webapp.dtos.InterestInDTO;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
import it.rate.webapp.models.*;
import it.rate.webapp.services.*;
import java.security.Principal;
import java.util.*;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController extends BaseThymeleafController {

  private final InterestService interestService;
  private final UserService userService;
  private final RoleService roleService;
  private final PermissionService permissionService;
  private final PlaceService placeService;
  private final LikeService likeService;
  private final CriterionService criterionService;
  private final CategoryService categoryService;

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @GetMapping("/create")
  public String createInterest(Model model) {

    Set<Criterion> criteria = new HashSet<>();
    model.addAttribute("criteria", criteria);
    model.addAttribute("interest", new Interest());
    model.addAttribute("action", "/interests/create");
    model.addAttribute("method", "post");
    model.addAttribute("categories", categoryService.findAll());

    return "interest/form";
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/create")
  public String createInterest(
      InterestInDTO interestDTO,
      Principal principal) {

    Interest interest = interestService.save(interestDTO);
    criterionService.saveAll(interest, interestDTO.criteriaNames());
    AppUser loggedUser = userService.getByEmail(principal.getName());
    roleService.setRole(interest, loggedUser, Role.RoleType.CREATOR);
    likeService.save(loggedUser, interest);

    return String.format("redirect:/interests/%d", interest.getId());
  }

  @GetMapping("/{interestId}")
  public String interestView(@PathVariable Long interestId, Model model, Principal principal) {

    Interest interest =
        interestService.findById(interestId).orElseThrow(InterestNotFoundException::new);
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      Optional<Role> optRole = roleService.findById(new RoleId(loggedUser.getId(), interestId));
      optRole.ifPresent(role -> model.addAttribute("role", role.getRoleType()));
      model.addAttribute(
          "liked", likeService.existsById(new LikeId(loggedUser.getId(), interestId)));
      model.addAttribute(
          "ratingPermission", permissionService.hasRatingPermission(loggedUser, interest));
    }
    model.addAttribute("interest", interest);
    model.addAttribute("places", placeService.getPlaceInfoDTOS(interest));

    return "interest/page";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/{interestId}/request")
  public String requestRatingAccess(@PathVariable Long interestId, Principal principal) {

    Interest interest =
        interestService.findById(interestId).orElseThrow(InvalidInterestDetailsException::new);
    AppUser loggedUser = userService.getByEmail(principal.getName());
    roleService.setRole(interest, loggedUser, Role.RoleType.APPLICANT);

    return "redirect:/interests/{interestId}";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @GetMapping("/my")
  public String myInterests(Model model, Principal principal) {

    AppUser loggedUser = userService.getByEmail(principal.getName());
    model.addAttribute("likedInterests", interestService.getLikedInterestsDTOS(loggedUser));

    return "interest/seeAll";
  }
}
