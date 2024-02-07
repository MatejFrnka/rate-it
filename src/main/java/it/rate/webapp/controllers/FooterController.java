package it.rate.webapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
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
  public String about() {
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
