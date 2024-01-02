package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.BadRequestException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {
  private final InterestService interestService;
  private final ManageInterestService manageInterestService;


  //todo ADD TESTS FOR EDIT
  @GetMapping("/edit")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String editInterestPage(@PathVariable Long interestId, Model model) {
    Optional<Interest> interest = interestService.findInterestById(interestId);
    if (interest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    model.addAttribute("interest", interest.get());
    model.addAttribute("action", "/interests/" + interestId + "/admin/edit");
    model.addAttribute("method", "put");
    return "interest/form";
  }

  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  @PutMapping("/edit")
  public String editInterest(
          @PathVariable Long interestId,
          @ModelAttribute Interest interest,
          @RequestParam List<String> criteriaNames,
          RedirectAttributes ra) {
    interest.setId(interestId);
    interestService.saveEditedInterest(interest, criteriaNames);
    ra.addAttribute("id", interestId);
    return "redirect:/interests/{id}";
  }

  @GetMapping("/users")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String editUsersPage(@PathVariable Long interestId, Model model)
      throws BadRequestException {
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    model.addAttribute("usersByRoles", manageInterestService.getUsersByRole(interestId));
    model.addAttribute("interest", optInterest.get());
    // todo: users template - form to invite users
    return "interest/users";
  }

  @DeleteMapping("/users/{userId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String removeUser(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.removeRole(interestId, userId);
    return "redirect:/interests/{interestId}/admin/users";
  }

  @PutMapping("/users/{userId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String acceptUser(@PathVariable Long interestId, @PathVariable Long userId) throws BadRequestException {
    manageInterestService.adjustRole(interestId, userId, Role.RoleType.VOTER);
    return "redirect:/interests/{interestId}/admin/users";
  }
}
