package it.rate.webapp.controllers;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseThymeleafController {

  //using autowired here to allow @AllArgsConstructor annotation on child controllers
  @Autowired
  private BuildProperties buildProperties;
  @Autowired
  private UserService userService;


  @ModelAttribute
  public void addCommonAttributes(Model model, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      model.addAttribute("loggedUser", loggedUser);
    }
    model.addAttribute("projectVersion", buildProperties.getVersion());
  }
}
