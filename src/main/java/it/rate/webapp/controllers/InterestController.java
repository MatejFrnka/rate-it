package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.CreateInterestService;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.RoleService;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController {

  private InterestService interestService;
  private CreateInterestService interestCreationService;
  private UserService userService;
  private RoleService roleService;

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
      @RequestParam String name,
      @RequestParam String description,
      @RequestParam List<String> criteriaNames,
      RedirectAttributes ra) {
    Interest savedInterest = interestCreationService.save(name, description, criteriaNames);
    ra.addAttribute("id", savedInterest.getId());
    return "redirect:/interests/{id}";
  }

  @GetMapping("/{interestId}")
  public String interestView(Model model, @PathVariable Long interestId, Principal principal) {
    Optional<Interest> interest = interestService.findInterestById(interestId);
    if (interest.isEmpty()) {
      model.addAttribute("message", "This interest doesn't exist");
      return "error/page";
    }

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());

      model.addAttribute("loggedUser", loggedUser);
      model.addAttribute("like", interestService.isLiked(loggedUser.getId(), interestId));

      Optional<Role> loggedUserRole =
          roleService.findByAppUserIdAndInterestId(loggedUser.getId(), interestId);
      if (loggedUserRole.isPresent()) {
        model.addAttribute("loggedUserRole", loggedUserRole.get());
      }
    }
    model.addAttribute("interest", interest.get());
    return "interest/page";
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/{interestId}/like")
  public String like(@PathVariable Long interestId, String likeOrDislike) {
    interestService.changeLikeValue(interestId, likeOrDislike);
    return "redirect:/interests/" + interestId;
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/{interestId}/request")
  public String applyForVoterAuthority(Interest interest) {
    interestService.setApplicantRole(interest);
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
              .orElseThrow(() -> new RuntimeException("Email not found in the database")));
      model.addAttribute(
          "likedInterests", interestService.getLikedInterestsDTOS(principal.getName()));
    }
    return "interest/seeAll";
  }
}
