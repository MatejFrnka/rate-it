package it.rate.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/interests/{interestId}/admin")
public class InterestAdminController {

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
  public String editUsersPage(@PathVariable Long interestId) {
    // todo: load Interest object by ID
    // todo: users template - form to invite users

    return "usersOfInterest";
  }

  @DeleteMapping("/users/{userId}")
  public String removeVoter(@PathVariable Long interestId, @PathVariable Long userId) {
    // todo: delete users role
    return "redirect:/{interestId}/users";
  }

  @DeleteMapping("/users/applicants/{userId}")
  public String rejectApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    // todo: delete user from List of applicants
    return "redirect:/{interestId}/users";
  }

  @PutMapping("/users/applicants/{userId}")
  public String acceptApplicant(@PathVariable Long interestId, @PathVariable Long userId) {
    // todo: create new Role(User/Interest)
    // todo: delete from List of applicants

    return "redirect:/{interestId}/users";
  }
}
