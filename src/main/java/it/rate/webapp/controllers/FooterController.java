package it.rate.webapp.controllers;

import it.rate.webapp.dtos.DeveloperDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(("/about"))
public class FooterController extends BaseThymeleafController {
  @GetMapping("/contact")
  public String contact() {
    return "footer/contact";
  }

  @GetMapping("/")
  public String about(Model model) {
    model.addAttribute("developers",
        List.of(
            new DeveloperDTO("Ben", "benedikt-vítek-45a86014a", "Full-Stack Developer"),
            new DeveloperDTO("dondo", "daniel-valasek", "Full-Stack Developer"),
            new DeveloperDTO("Martin", "martin-hubner", "Full-Stack Developer"),
            new DeveloperDTO("vdrfg", "jan-benda-7b4aab142", "Full-Stack Developer"),
            new DeveloperDTO("Matej", "matej-frnka", "Tech lead")
        ));
    return "footer/about";
  }

  @GetMapping("/privacy-policy")
  public String privacy() {
    return "footer/legal/privacyPolicy";
  }

  @GetMapping("/terms-and-conditions")
  public String terms() {
    return "footer/legal/termsAndConditions";
  }
}
