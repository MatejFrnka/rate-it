package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.repositories.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final Validator validator;

  public AppUser save(AppUser appUser) {
    return userRepository.save(appUser);
  }

  public AppUser registerUser(SignupUserInDTO userDTO) {
    Set<ConstraintViolation<SignupUserInDTO>> violations = validator.validate(userDTO);
    if (!violations.isEmpty()) {
      throw new InvalidUserDetailsException(violations.stream().findFirst().get().getMessage());
    }
    if (userRepository.existsByEmailIgnoreCase(userDTO.email())) {
      throw new UserAlreadyExistsException(
          "User with email " + userDTO.email() + " already exists");
    }
    if (userRepository.existsByUsernameIgnoreCase(userDTO.username())) {
      throw new UserAlreadyExistsException(
          "User with username " + userDTO.username() + " already exists");
    }

    String hashPassword = passwordEncoder.encode(userDTO.password());
    AppUser user =
        AppUser.builder()
            .username(userDTO.username())
            .email(userDTO.email())
            .password(hashPassword)
            .serverRole(ServerRole.USER)
            .build();
    return userRepository.save(user);
  }

  public Optional<AppUser> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<AppUser> findByUsernameIgnoreCase(String username) {
    return userRepository.findByUsernameIgnoreCase(username);
  }

  public AppUser authenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication.getPrincipal().equals("anonymousUser"))) {
      return userRepository.getByEmail(authentication.getName());
    }
    return null;
  }

  public Optional<AppUser> findByEmailIgnoreCase(String email) {
    return userRepository.findByEmailIgnoreCase(email);
  }

  public AppUser getByEmail(String email) {
    return userRepository.getByEmail(email);
  }

  public Optional<AppUser> findById(Long userId) {
    return userRepository.findById(userId);
  }
}
