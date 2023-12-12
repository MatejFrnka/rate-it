package it.rate.webapp.controllers;

import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController {

  private InterestService service;

  @GetMapping("/create")
  public String createPage() {
    // todo: send empty arraylist as an attribute. For criteria.
    return "interestForm";
  }

  @PostMapping("/create")
  public String createNew() {
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
  public String applyForVoterAuthority() {
    // todo: add logged in user to list of request of interest
    // todo: redirect to interest page by id
    return "redirect:/interests/{id}";
  }
}
