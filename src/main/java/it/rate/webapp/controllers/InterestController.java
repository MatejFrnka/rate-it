package it.rate.webapp.controllers;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.CreateInterestService;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/interests")
public class InterestController {

  private InterestService service;
  private CreateInterestService interestCreationService;

  @GetMapping("/create")
  public String createPage(Model model) {
    List<Criterion> criteria = new ArrayList<>();
    model.addAttribute("criteria", criteria);
    return "interestForm";
  }

  @PostMapping("/create")
  public String createNew(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam List<String> criteria,
                          RedirectAttributes ra) {
    Interest savedInterest = interestCreationService.save(name, description, criteria);
    ra.addAttribute("id", savedInterest.getId());
    // todo: assign user
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
}
