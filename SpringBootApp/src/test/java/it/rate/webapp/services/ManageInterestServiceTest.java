package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.enums.InviteBy;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManageInterestServiceTest extends BaseTest {

  @MockBean RoleRepository roleRepository;
  @MockBean UserService userService;

  @Autowired ManageInterestService manageInterestService;

  Interest i1;
  AppUser u1;
  AppUser u2;
  AppUser u3;

  @BeforeEach
  void setUp() {
    u1 = AppUser.builder().username("Lojza").id(1L).password("Password1").email("l@l.com").build();

    i1 = Interest.builder().id(1L).name("IT kurzy").description("IT kurzy").exclusive(true).build();

    u2 = AppUser.builder().username("Alfonz").id(2L).build();

    u3 = AppUser.builder().username("Karel").id(3L).build();

    Role r1 = new Role(u1, i1, Role.RoleType.CREATOR);
    Role r2 = new Role(u2, i1, Role.RoleType.VOTER);
    Role r3 = new Role(u3, i1, Role.RoleType.APPLICANT);

    i1.setRoles(List.of(r1, r2, r3));
  }

  @Test
  void inviteUserInvalidParameters() {
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(null, null, null, null));
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, null, null, null));
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(null, InviteBy.EMAIL, null, null));
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(null, null, "franta", null));
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(null, null, null, Role.RoleType.APPLICANT));
    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, InviteBy.EMAIL, null, null));

    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, null, "franta", null));

    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, null, null, Role.RoleType.APPLICANT));

    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, InviteBy.EMAIL, "franta", null));

    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, InviteBy.EMAIL, null, Role.RoleType.APPLICANT));

    assertThrows(
        ConstraintViolationException.class,
        () -> manageInterestService.inviteUser(i1, null, "franta", Role.RoleType.APPLICANT));
  }

  @Test
  void inviteUserNonExistingUser() {
    Exception e =
        assertThrows(
            InvalidUserDetailsException.class,
            () ->
                manageInterestService.inviteUser(
                    i1, InviteBy.USERNAME, "franta", Role.RoleType.VOTER));

    assertEquals("User with given details not found", e.getMessage());
  }

  @Test
  void createNewRoleNonExistingUser() {
    when(userService.findByUsernameIgnoreCase(any())).thenReturn(Optional.empty());
    when(userService.findByUsernameIgnoreCase(any())).thenReturn(Optional.empty());

    assertThrows(
        InvalidUserDetailsException.class,
        () ->
            manageInterestService.inviteUser(
                i1, InviteBy.USERNAME, "franta", Role.RoleType.APPLICANT));

    assertThrows(
        InvalidUserDetailsException.class,
        () ->
            manageInterestService.inviteUser(
                i1, InviteBy.EMAIL, "test@test.cz", Role.RoleType.APPLICANT));
  }

  @Test
  void inviteUserReturnsRoleForValidParameters()
      throws InvalidInterestDetailsException, InvalidUserDetailsException {
    AppUser userWithoutRole = new AppUser();
    Role.RoleType roleToCreate = Role.RoleType.VOTER;
    when(userService.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(userWithoutRole));
    when(roleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Role newRole = manageInterestService.inviteUser(i1, InviteBy.USERNAME, "franta", roleToCreate);

    verify(roleRepository, times(1)).save(newRole);
    verify(userService, times(1)).findByUsernameIgnoreCase("franta");

    assertEquals(roleToCreate, newRole.getRoleType());
    assertEquals(userWithoutRole, newRole.getAppUser());
  }

  @Test
  void inviteUserThrowsExceptionForExistingMember() {
    when(userService.findByUsernameIgnoreCase("Lojza")).thenReturn(Optional.of(u1));
    when(userService.findByUsernameIgnoreCase("Alfonz")).thenReturn(Optional.of(u2));

    assertThrows(
        UserAlreadyExistsException.class,
        () ->
            manageInterestService.inviteUser(i1, InviteBy.USERNAME, "Lojza", Role.RoleType.VOTER));
    assertThrows(
        UserAlreadyExistsException.class,
        () ->
            manageInterestService.inviteUser(i1, InviteBy.USERNAME, "Alfonz", Role.RoleType.VOTER));
  }

  @Test
  void inviteUserReturnsValidRoleForApplicant() {
    Role role = new Role(u3, i1, Role.RoleType.VOTER);
    when(roleRepository.save(any())).thenAnswer(arg -> arg.getArgument(0));
    when(userService.findByUsernameIgnoreCase("Karel")).thenReturn(Optional.of(u3));

    assertEquals(
        role.getAppUser(),
        manageInterestService
            .inviteUser(i1, InviteBy.USERNAME, "Karel", Role.RoleType.VOTER)
            .getAppUser());
    assertEquals(
        role.getRoleType(),
        manageInterestService
            .inviteUser(i1, InviteBy.USERNAME, "Karel", Role.RoleType.VOTER)
            .getRoleType());
  }

  @Test
  void inviteUserReturnsValidRoleForNoRoleUser() {
    AppUser u4 = AppUser.builder().username("Alfred").build();

    Role role = new Role(u4, i1, Role.RoleType.VOTER);
    when(roleRepository.save(any())).thenAnswer(arg -> arg.getArgument(0));
    when(userService.findByUsernameIgnoreCase("Alfred")).thenReturn(Optional.of(u4));

    assertEquals(
        role.getAppUser(),
        manageInterestService
            .inviteUser(i1, InviteBy.USERNAME, "Alfred", Role.RoleType.VOTER)
            .getAppUser());
    assertEquals(
        role.getRoleType(),
        manageInterestService
            .inviteUser(i1, InviteBy.USERNAME, "Alfred", Role.RoleType.VOTER)
            .getRoleType());
  }

  @Test
  void mapInviteThrowsExceptionForInvalidInput() {
    assertThrows(BadRequestException.class, () -> manageInterestService.mapInvite("USERNAAAME"));
    assertThrows(BadRequestException.class, () -> manageInterestService.mapInvite("EMAAIL"));
  }

  @Test
  void mapInviteReturnsInviteByForValidInput() {
    InviteBy username = manageInterestService.mapInvite("USERNAME");
    assertEquals(InviteBy.USERNAME, username);

    InviteBy email = manageInterestService.mapInvite("EMAIL");
    assertEquals(InviteBy.EMAIL, email);
  }
}
