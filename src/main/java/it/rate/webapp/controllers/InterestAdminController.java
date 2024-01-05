package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.ManageInterestService;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {
  private final InterestService interestService;
  private final ManageInterestService manageInterestService;
  private final UserService userService;

  @GetMapping("/edit")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String editInterestPage(@PathVariable Long interestId, Model model, Principal principal) {
    Optional<Interest> interest = interestService.findInterestById(interestId);
    if (interest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    model.addAttribute("interest", interest.get());
    model.addAttribute("action", "/interests/" + interestId + "/admin/edit");
    model.addAttribute("method", "put");
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
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
  public String editUsersPage(@PathVariable Long interestId, Model model, Principal principal)
      throws BadRequestException {
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    if (principal != null) {
      model.addAttribute(
          "loggedUser",
          userService
              .findByEmail(principal.getName())
              .orElseThrow(() -> new RuntimeException("Email not found in the database")));
    }

    model.addAttribute("usersByRoles", manageInterestService.getUsersByRole(interestId));
    //todo remove usersByRoles and amend tests to use new method
    model.addAttribute("interest", optInterest.get());
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
  public String acceptUser(@PathVariable Long interestId, @PathVariable Long userId)
      throws BadRequestException {
    manageInterestService.adjustRole(interestId, userId, Role.RoleType.VOTER);
    return "redirect:/interests/{interestId}/admin/users";
  }

  @GetMapping("/invite")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public String inviteUsers(@PathVariable Long interestId, Model model, Principal principal)
          throws BadRequestException {
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    if (principal != null) {
      model.addAttribute(
              "loggedUser",
              userService
                      .findByEmail(principal.getName())
                      .orElseThrow(() -> new RuntimeException("Email not found in the database")));
    }

    model.addAttribute("usersByRoles", manageInterestService.getUsersByRole(interestId));
    model.addAttribute("interest", optInterest.get());
    return "interest/invite";
  }
}
