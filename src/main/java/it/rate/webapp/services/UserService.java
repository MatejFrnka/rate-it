package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.UserAlreadyExistsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.repositories.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AppUser save(AppUser appUser) {
    return userRepository.save(appUser);
  }

  public AppUser registerUser(SignupUserInDTO userDTO)
      throws UserAlreadyExistsException, IllegalArgumentException {
    if (!isValidRegistration(userDTO)) {
      throw new IllegalArgumentException("Invalid registration data");
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

  public boolean isValidRegistration(SignupUserInDTO userDTO) {
    return userDTO != null
        && userDTO.email() != null
        && userDTO.password() != null
        && userDTO.username() != null
        && !userDTO.email().isBlank()
        && !userDTO.password().isBlank()
        && !userDTO.username().isBlank();
    // todo check if email is valid and password is strong
  }

  public Optional<AppUser> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public AppUser getByEmail(String email) {
    return userRepository.getByEmail(email);
  }

  public Optional<AppUser> findById(Long userId) {
    return userRepository.findById(userId);
  }
}
