package it.rate.webapp.controllers;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController {

  private InterestService service;

  @GetMapping("/create")
  public String createPage(Model model) {
    List<Criterion> criteria = new ArrayList<>();
    model.addAttribute("criteria", criteria);
    return "interestForm";
  }

  @PostMapping("/create")
  public String createNew(Interest interest, List<Criterion> criteria) {
    // todo: accept model of Interest(subsite), accept list of criteria (if possible?)
    // todo: add business logic to connect criteria with new subject and save them into DB
    // todo: redirect to /{id}

    return "redirect:/interests/{id}";
  }

  @GetMapping("/{id}")
  public String interestView(Model model, @PathVariable Long id) {
    Optional<Interest> interest = service.findInterestById(id);
    if (interest.isEmpty()) {
      model.addAttribute("message", "This interest doesn't exist");
      return "errorPage";
    }
    model.addAttribute("interest", interest.get());
    return "interest";
  }

  @PostMapping("/{id}/vote")
  public String vote(@PathVariable Long id) {
    // todo: change vote value according to input (either delete vote, create new one or change vote
    // value)
    // todo: redirect to page where user voted
    return "todo";
  }

  @PostMapping("/{id}/voterauthorityrequest")
  public String applyForVoterAuthority(@PathVariable Long id) {
    service.setApplicantRole(id);
    // todo: add logged user to the method
    // todo: redirect might not be necessary with the use of js?
    return "redirect:/interests/{id}";
  }

  /*
  ONLY TESTING
  ENDPOINTS
  FOR ROLE BASED ACCESS
  BELOW
   */

  @GetMapping("/{id}/creators")
  @PreAuthorize("hasAuthority('ROLE_CREATOR_' + #id)")
  public String creatorPage(@PathVariable Long id) {
    return "test-role-base-access";
  }

  @GetMapping("/{id}/voters")
  @PreAuthorize("hasAuthority('ROLE_VOTER_' + #id)")
  public String voterPage(@PathVariable Long id) {
    return "test-role-base-access";
  }

  @GetMapping("/{id}/both")
  @PreAuthorize("hasAnyAuthority('ROLE_VOTER_' + #id, 'ROLE_CREATOR_' + #id)")
  public String voterAndCreatorPage(@PathVariable Long id) {
    return "test-role-base-access";
  }

  @GetMapping("/{id}/bothandadmin")
  @PreAuthorize("hasAnyAuthority('ROLE_VOTER_' + #id, 'ROLE_CREATOR_' + #id, 'ADMIN')")
  public String adminAndVoterCreatorPage(@PathVariable Long id) {
    return "test-role-base-access";
  }
}
