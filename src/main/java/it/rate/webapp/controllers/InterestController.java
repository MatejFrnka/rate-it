package it.rate.webapp.controllers;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
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
      return "nullInterest";
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
}
