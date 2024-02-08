package it.rate.webapp.controllers;

import it.rate.webapp.dtos.InterestInDTO;
import it.rate.webapp.enums.InviteBy;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController extends BaseThymeleafController {
  private final InterestService interestService;
  private final ManageInterestService manageInterestService;
  private final RoleService roleService;
  private final UserService userService;
  private final CriterionService criterionService;
  private final CategoryService categoryService;
  private final EmailService emailService;

  @GetMapping("/edit")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String editInterestPage(@PathVariable Long interestId, Model model) {

    model.addAttribute("interest", interestService.getById(interestId));
    model.addAttribute("action", "/interests/" + interestId + "/admin/edit");
    model.addAttribute("method", "put");
    model.addAttribute("categories", categoryService.findAll());

    return "interest/form";
  }

  @Transactional
  @PutMapping("/edit")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String editInterest(@PathVariable Long interestId, InterestInDTO interestDTO) {

    Interest interest = interestService.getById(interestId);
    interest = interestService.update(interest, interestDTO);
    criterionService.updateAll(interest, interestDTO.criteriaNames());

    return String.format("redirect:/interests/%d", interestId);
  }

  @GetMapping("/users")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String editUsersPage(@PathVariable Long interestId, Model model) {
    model.addAttribute("interest", interestService.getById(interestId));

    return "interest/users";
  }

  @DeleteMapping("/users/{userId}")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String removeUser(@PathVariable Long interestId, @PathVariable Long userId) {

    roleService.removeRole(interestId, userId);

    return "redirect:/interests/{interestId}/admin/users";
  }

  @PutMapping("/users/{userId}")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String acceptUser(@PathVariable Long interestId, @PathVariable Long userId) {

    AppUser user = userService.getById(userId);
    Interest interest = interestService.getById(interestId);
    roleService.setRole(interest, user, Role.RoleType.VOTER);

    return "redirect:/interests/{interestId}/admin/users";
  }

  @GetMapping("/invite")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String inviteUsers(@PathVariable Long interestId, Model model) {
    model.addAttribute("interest", interestService.getById(interestId));
    return "interest/invite";
  }

  @PostMapping("/invite")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public String inviteUser(
      @PathVariable Long interestId, String inviteBy, String user, RedirectAttributes ra) {

    Interest interest = interestService.getById(interestId);
    InviteBy invite = manageInterestService.mapInvite(inviteBy);
    try {
      Role role = manageInterestService.inviteUser(interest, invite, user, Role.RoleType.VOTER);
      ra.addFlashAttribute("status", "Invitation successfully sent");
      ra.addFlashAttribute("statusClass", "successful");
      ra.addFlashAttribute("isChecked", invite == InviteBy.USERNAME);
      emailService.sendInvite(role);
    } catch (BadRequestException e) {
      ra.addFlashAttribute("status", e.getMessage());
      ra.addFlashAttribute("statusClass", "error");
      ra.addFlashAttribute("user", user);
      ra.addFlashAttribute("isChecked", invite == InviteBy.USERNAME);
    }

    return "redirect:/interests/{interestId}/admin/invite";
  }
}
