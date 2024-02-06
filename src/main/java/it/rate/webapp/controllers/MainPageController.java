package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.CategoryService;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Validated
@RequiredArgsConstructor
public class MainPageController extends BaseThymeleafController {

  private final InterestService interestService;
  private final UserService userService;
  private final CategoryService categoryService;
  private final BuildProperties buildProperties;

  @GetMapping({"/", "/index"})
  public String index(Model model, Principal principal) {

    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
      model.addAttribute("likedInterests", interestService.findAllLikedByAppUser(loggedUser));
    }
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("projectVersion", buildProperties.getVersion());

    return "main/index";
  }
}
