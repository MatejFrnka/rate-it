package it.rate.webapp.controllers;

import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.ManageInterestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {
  private final InterestService interestService;
  private final ManageInterestService manageInterestService;

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
    manageInterestService.removeRole(interestId, userId);
    return "redirect:/interests/{interestId}/admin/users";
  }

  @DeleteMapping("/users/applicants/{userId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String rejectApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.removeRole(interestId, userId);
    // todo: // Same method as above (endpoint kept for eventual logging of rejected users)
    return "redirect:/interests/{interestId}/admin/users";
  }

  @PutMapping("/users/applicants/{userId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String acceptApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.adjustRole(interestId, userId, Role.RoleType.VOTER);
    return "redirect:/interests/{interestId}/admin/users";
  }
}
