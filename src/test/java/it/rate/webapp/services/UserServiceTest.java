package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.rate.webapp.BaseTest;
import it.rate.webapp.dtos.SignupUserInDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class UserServiceTest extends BaseTest {

  @Autowired UserService userService;
  @MockBean UserRepository userRepository;

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
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "u");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(true);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
    assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
  }

  @Test
  void registerUserUsernameExists() {
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "u");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(true);
    assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
  }

  @Test
  void registerUserValid() throws BadRequestException {
    SignupUserInDTO user = new SignupUserInDTO("a@b.c", "Password123", "u");
    when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
    when(userRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
    userService.registerUser(user);

    verify(userRepository, times(1)).save(any());
  }
}
