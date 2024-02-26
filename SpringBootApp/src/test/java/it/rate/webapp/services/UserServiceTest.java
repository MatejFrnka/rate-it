package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.rate.webapp.BaseTest;
import it.rate.webapp.dtos.PasswordResetDTO;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidTokenException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.PasswordReset;
import it.rate.webapp.repositories.PasswordResetRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class UserServiceTest extends BaseTest {

  @Autowired UserService userService;
  @MockBean UserRepository userRepository;
  @MockBean PasswordEncoder passwordEncoder;
  @MockBean PasswordResetRepository passwordResetRepository;

  @Test
  void registerUserSaveInvalidUser() throws UserAlreadyExistsException {
    SignupUserInDTO invalidUser = new SignupUserInDTO(null, null, null);
    assertThrows(BadRequestException.class, () -> userService.registerUser(invalidUser));
  }

  @Test
  void registerUserSaveInvalidUserEmail() throws UserAlreadyExistsException {
    SignupUserInDTO invalidUser =
        new SignupUserInDTO("invalid email", "VALID PASSWORD", "valid username");
    assertThrows(BadRequestException.class, () -> userService.registerUser(invalidUser));
  }

  @Test
  void registerUserEmailExists() {
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "username");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(true);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
    assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
  }

  @Test
  void registerUserUsernameExists() {
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "username");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(true);
    assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
  }

  @Test
  void registerUserValid() throws BadRequestException {
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "username");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
    userService.registerUser(user);

    verify(userRepository, times(1)).save(any());
  }

  @Test
  void updatePassword_WithValidPassword_ChangesPassword() {
    AppUser user = AppUser.builder().id(1L).password("Password123").build();
    PasswordResetDTO pwResetDTO = new PasswordResetDTO("token", 1L, "NewPassword123");
    PasswordReset pwReset = new PasswordReset(user, "hashedToken");

    when(passwordEncoder.encode("NewPassword123")).thenReturn("EncodedNewPassword123");
    when(passwordResetRepository.findByUser_Id(anyLong())).thenReturn(Optional.of(pwReset));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    userService.updatePassword(pwResetDTO);

    verify(userRepository, times(1)).save(eq(user));
    verify(passwordResetRepository, times(1)).delete(eq(pwReset));
    assertEquals("EncodedNewPassword123", user.getPassword());
    assertNull(user.getPasswordReset());
  }

  @Test
  void updatePassword_WithInvalidPassword_ThrowsException() {
    PasswordResetDTO pwResetDTO = new PasswordResetDTO("token", 1L, "invalidPassword");
    assertThrows(InvalidUserDetailsException.class, () -> userService.updatePassword(pwResetDTO));
  }

  @Test
  void validateToken_WithInvalidToken_ThrowsException() {
    when(passwordResetRepository.findByUser_Id(anyLong()))
        .thenReturn(Optional.of(new PasswordReset()));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    assertThrows(InvalidTokenException.class, () -> userService.validateToken("invalidToken", 1L));
  }
}
