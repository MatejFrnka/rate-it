package it.rate.webapp.services;

import it.rate.webapp.config.ServerRole;
import it.rate.webapp.dtos.AppUserDTO;
import it.rate.webapp.dtos.InterestUserDTO;
import it.rate.webapp.dtos.PasswordResetDTO;
import it.rate.webapp.dtos.PasswordResetEmailDTO;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidTokenException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.exceptions.unauthorised.ForbiddenOperationException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.PasswordResetRepository;
import it.rate.webapp.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final Validator validator;
  private final AuthenticationManager provider;
  private final PasswordResetRepository passwordResetRepository;
  private final EmailService emailService;
  private final ImageService imageService;

  public Optional<AppUser> findById(Long userId) {
    return userRepository.findById(userId);
  }

  public AppUser getByEmail(String email) {
    return userRepository.getByEmail(email);
  }

  public AppUser getById(Long id) {
    return userRepository.getReferenceById(id);
  }

  public Optional<AppUser> findByUsernameIgnoreCase(String username) {
    return userRepository.findByUsernameIgnoreCase(username);
  }

  public Optional<AppUser> findByEmailIgnoreCase(@NotNull @Email String email) {
    return userRepository.findByEmailIgnoreCase(email);
  }

  public List<AppUser> findAllDistinctByPlace(@Valid Place place) {
    return userRepository.findAllDistinctByReviews_PlaceOrRatings_Place(place);
  }

  public List<InterestUserDTO> getUsersDTO(
      @Valid Interest interest, @NotNull Role.RoleType roleType) {
    return interest.getRoles().stream()
        .filter(r -> r.getRoleType().equals(roleType))
        .map(InterestUserDTO::new)
        .sorted(Comparator.comparing(dto -> dto.userName().toLowerCase()))
        .collect(Collectors.toList());
  }

  public void registerUser(SignupUserInDTO userDTO) {
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
    userRepository.save(user);
  }

  public void authenticate(String username, String password, HttpSession session) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
    Authentication authenticated = provider.authenticate(authentication);

    SecurityContextHolder.getContext().setAuthentication(authenticated);

    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        SecurityContextHolder.getContext());
  }

  public AppUser getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication.getPrincipal().equals("anonymousUser"))) {
      return userRepository.getByEmail(authentication.getName());
    }
    return null;
  }

  public void follow(@Valid AppUser follower, @Valid AppUser followed, boolean follow)
      throws BadRequestException {
    if (follower.equals(followed)) {
      throw new BadRequestException("Users cannot follow themselves!");
    }
    if (follow) {
      follower.getFollows().add(followed);
    } else {
      follower.getFollows().remove(followed);
    }
    userRepository.save(follower);
  }

  public void editUser(@Valid AppUserDTO editedUser) {
    AppUser appUser = getAuthenticatedUser();
    if (appUser == null) {
      throw new ForbiddenOperationException();
    }
    appUser.setBio(editedUser.bio());
    userRepository.save(appUser);
  }

  public void initPasswordReset(@Valid AppUser user) {
    UUID uuid = UUID.randomUUID();
    Optional<PasswordReset> optReset = passwordResetRepository.findByUser(user);
    if (optReset.isPresent()) {
      PasswordReset pwReset = optReset.get();
      pwReset.updateToken(passwordEncoder.encode(uuid.toString()));
      passwordResetRepository.save(pwReset);
    } else {
      passwordResetRepository.save(
          new PasswordReset(user, passwordEncoder.encode(uuid.toString())));
    }
    emailService.sendPasswordReset(new PasswordResetEmailDTO(user, uuid.toString()));
  }

  public PasswordReset validateToken(@NotBlank String token, @NotNull Long ref) {
    PasswordReset pwReset =
        passwordResetRepository
            .findByUser_Id(ref)
            .orElseThrow(() -> new InvalidTokenException("Invalid token"));

    if (!passwordEncoder.matches(token, pwReset.getToken())) {
      throw new InvalidTokenException("Invalid token");
    }
    if (pwReset.getExpiration().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("Token has expired. Please request new password reset");
    }
    return pwReset;
  }

  @Transactional
  public void updatePassword(@NotNull PasswordResetDTO pwResetDTO) {
    Set<ConstraintViolation<PasswordResetDTO>> violations =
        validator.validateProperty(pwResetDTO, "password");
    if (!violations.isEmpty()) {
      throw new InvalidUserDetailsException(violations.stream().findFirst().get().getMessage());
    }

    PasswordReset pwReset = validateToken(pwResetDTO.token(), pwResetDTO.ref());
    AppUser user = pwReset.getUser();
    user.setPassword(passwordEncoder.encode(pwResetDTO.password()));
    user.setPasswordReset(null);
    userRepository.save(user);
    passwordResetRepository.delete(pwReset);
  }

  public void addImage(@NotNull String imageId, AppUser user) {
    if (user.getImageName() != null) {
      imageService.deleteById(user.getImageName());
    }
    user.setImageName(imageId);
    userRepository.save(user);
  }
}
