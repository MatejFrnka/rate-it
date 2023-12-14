package it.rate.webapp.controllers;

import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.ManageInterestService;
import it.rate.webapp.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {
  private InterestService interestService;
  private ManageInterestService manageInterestService;

  public InterestAdminController(InterestService interestService, ManageInterestService manageInterestService) {
    this.interestService = interestService;
    this.manageInterestService = manageInterestService;
  }

  @GetMapping("/edit")
  public String editPageView(Model model, @PathVariable Long interestId) {

    // todo: add model attribute(Interest) according to id
    // todo: return page customization/create template
    return "interestForm";
  }

  @PutMapping("/edit")
  public String editPage(@PathVariable Long interestId) {
    // todo: update Interest object according to input changes
    // todo: redirect to GET interest page
    return "redirect:/interests/{interestId}/";
  }
  @GetMapping("/users")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String editUsersPage(@PathVariable Long interestId, Model model) {
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    if (optInterest.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    model.addAttribute("usersByRoles", manageInterestService.getUsersByRole(interestId));
    model.addAttribute("interest", optInterest.get());
    // todo: users template - form to invite users
    return "usersOfInterest";
  }

  @DeleteMapping("/users/{userId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String removeVoter(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.removeVoter(interestId, userId);
    return "redirect:../users";
  }

  @DeleteMapping("/users/applicants/{userId}")
  public String rejectApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    // todo: delete user from List of applicants
    return "redirect:/{interestId}/users";
  }

  @PutMapping("/users/applicants/{userId}")
  public String acceptApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    // todo: create new Role(User/Interest)

    return "redirect:/{interestId}/users";
  }
}
