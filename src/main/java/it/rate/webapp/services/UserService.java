package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.repositories.UserRepository;
import jakarta.validation.Validator;
import java.util.Optional;
import lombok.AllArgsConstructor;
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

  public AppUser registerUser(SignupUserInDTO userDTO) throws BadRequestException {
    if (!validator.validate(userDTO).isEmpty()) {
      throw new BadRequestException("Invalid registration data");
    }
    if (userRepository.existsByEmail(userDTO.email())) {
      throw new UserAlreadyExistsException(
          "User with email " + userDTO.email() + " already exists");
    }
    if (userRepository.existsByUsername(userDTO.username())) {
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
