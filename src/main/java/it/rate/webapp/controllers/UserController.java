package it.rate.webapp.controllers;

import it.rate.webapp.dtos.*;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
import it.rate.webapp.exceptions.notfound.UserNotFoundException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController extends BaseThymeleafController {

  private final UserService userService;
  private final InterestService interestService;
  private final PlaceService placeService;

  @GetMapping("/signup")
  public String signupPage() {
    return "user/signupForm";
  }

  @PostMapping("/signup")
  public String newUser(
      SignupUserInDTO userDTO, Model model, String confirmPassword, HttpServletRequest request) {
    if (!confirmPassword.equals(userDTO.password())) {
      model.addAttribute("error", "Passwords do not match. Please try again.");
      model.addAttribute("userDTO", new SignupUserOutDTO(userDTO));
      return "user/signupForm";
    }

    try {
      userService.registerUser(userDTO);
    } catch (BadRequestException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("userDTO", new SignupUserOutDTO(userDTO));
      return "user/signupForm";
    }

    userService.authenticate(userDTO.email(), userDTO.password(), request.getSession(true));
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginPage(Principal principal) {
    if (principal != null) {
      return "redirect:/";
    }
    return "user/loginForm";
  }

  @GetMapping("/reset")
  public String resetPassword(String token, Long ref, Model model, Principal principal) {
    if (token == null || ref == null) {
      return principal != null ? "redirect:/" : "user/resetPassword";
    }
    userService.validateToken(token, ref);

    model.addAttribute("token", token);
    model.addAttribute("ref", ref);
    return "user/resetPasswordForm";
  }

  @PostMapping("/reset")
  public String submitResetPassword(Model model, String email) {
    Optional<AppUser> user = userService.findByEmailIgnoreCase(email);
    user.ifPresent(userService::initPasswordReset);

    model.addAttribute("email", email);
    return "user/resetSubmitted";
  }

  @PutMapping("/reset")
  public String updatePassword(PasswordResetDTO pwResetDTO, String confirmPassword, Model model) {
    if (!confirmPassword.equals(pwResetDTO.password())) {
      model.addAttribute("error", "Passwords do not match. Please try again.");
      model.addAttribute("token", pwResetDTO.token());
      model.addAttribute("ref", pwResetDTO.ref());
      return "user/resetPasswordForm";
    }
    try {
      userService.updatePassword(pwResetDTO);
    } catch (InvalidUserDetailsException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("token", pwResetDTO.token());
      model.addAttribute("ref", pwResetDTO.ref());
      return "user/resetPasswordForm";
    }
    return "redirect:/login";
  }

  @GetMapping("/users/{username}")
  public String userPage(@PathVariable String username, Model model) {
    AppUser user =
        userService.findByUsernameIgnoreCase(username).orElseThrow(UserNotFoundException::new);
    List<RatedInterestDTO> ratedInterests = interestService.getAllRatedInterestsDTOS(user);

    model.addAttribute("user", new AppUserDTO(user));
    model.addAttribute("ratedInterests", ratedInterests);

    return "user/page";
  }

  @GetMapping("/users/{username}/interests/{interestId}")
  public String interestDetail(
      @PathVariable String username,
      @PathVariable Long interestId,
      Model model) {

    AppUser user =
        userService.findByUsernameIgnoreCase(username).orElseThrow(UserNotFoundException::new);
    Interest interest =
        interestService.findById(interestId).orElseThrow(InterestNotFoundException::new);
    Comparator<PlaceReviewDTO> comparator =
        Comparator.comparing(PlaceReviewDTO::timestamp).reversed();
    List<PlaceReviewDTO> placesReviews =
        placeService.getPlaceReviewDTOs(user, interest, comparator);

    model.addAttribute("user", new AppUserDTO(user));
    model.addAttribute("interest", interest);
    model.addAttribute("placesReviews", placesReviews);

    return "user/interest";
  }
}
