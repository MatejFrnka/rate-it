package it.rate.webapp.controllers;

import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.ManageInterestService;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {
  private final InterestService interestService;
  private final ManageInterestService manageInterestService;
  private final UserService userService;

  @GetMapping("/edit")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String editInterestPage(@PathVariable Long interestId, Model model, Principal principal) {
    model.addAttribute("interest", interestService.getById(interestId));
    model.addAttribute("action", "/interests/" + interestId + "/admin/edit");
    model.addAttribute("method", "put");
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    return "interest/form";
  }

  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
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
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String editUsersPage(@PathVariable Long interestId, Model model, Principal principal) {
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    model.addAttribute("interest", interestService.getById(interestId));
    return "interest/users";
  }

  @DeleteMapping("/users/{userId}")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String removeUser(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.removeRole(interestId, userId);
    return "redirect:/interests/{interestId}/admin/users";
  }

  @PutMapping("/users/{userId}")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String acceptUser(@PathVariable Long interestId, @PathVariable Long userId) {
    manageInterestService.adjustRole(interestId, userId, Role.RoleType.VOTER);
    return "redirect:/interests/{interestId}/admin/users";
  }

  @GetMapping("/invite")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String inviteUsers(@PathVariable Long interestId, Model model, Principal principal) {
    if (principal != null) {
      model.addAttribute("loggedUser", userService.getByEmail(principal.getName()));
    }
    model.addAttribute("interest", interestService.getById(interestId));
    return "interest/invite";
  }

  @PostMapping("/invite")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String inviteUser(
      @PathVariable Long interestId, String inviteBy, String user, RedirectAttributes ra) {
    try {
      manageInterestService.inviteUser(interestId, inviteBy, user, Role.RoleType.VOTER);
      ra.addFlashAttribute("status", "Invite successfully sent");
      ra.addFlashAttribute("statusClass", "successful");
      ra.addFlashAttribute("isChecked", inviteBy.equals("username"));
    } catch (BadRequestException e) {
      ra.addFlashAttribute("status", e.getMessage());
      ra.addFlashAttribute("statusClass", "error");
      ra.addFlashAttribute("user", user);
      ra.addFlashAttribute("isChecked", inviteBy.equals("username"));
    }
    return "redirect:/interests/{interestId}/admin/invite";
  }
}
