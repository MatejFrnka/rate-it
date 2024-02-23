package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.ServerRole;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidRoleDetailsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import it.rate.webapp.repositories.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceTest extends BaseTest {
  @MockBean RoleRepository roleRepository;
  @Autowired RoleService roleService;

  Interest i1;
  AppUser u1;

  @BeforeEach
  void setUp() {
    u1 =
        AppUser.builder()
            .username("Lojza")
            .id(1L)
            .password("Password1")
            .email("l@l.com")
            .serverRole(ServerRole.USER)
            .build();

    i1 = Interest.builder().id(1L).name("IT kurzy").description("IT kurzy").exclusive(true).build();

    Role r1 = new Role(u1, i1, Role.RoleType.VOTER);

    i1.setRoles(List.of(r1));
  }

  @Test
  void removeRoleInvalidParams() {
    assertThrows(ConstraintViolationException.class, () -> roleService.removeRole(null, null));
    assertThrows(ConstraintViolationException.class, () -> roleService.removeRole(null, 1L));
    assertThrows(ConstraintViolationException.class, () -> roleService.removeRole(1L, null));
  }

  @Test
  void removeRoleRemoveCreatorRoleThrowsException() {
    Role roleToDelete = new Role(u1, i1, Role.RoleType.CREATOR);
    when(roleService.findById(any())).thenReturn(Optional.of(roleToDelete));

    Exception e1 =
        assertThrows(InvalidRoleDetailsException.class, () -> roleService.removeRole(1L, 1L));
    assertEquals("Cannot remove creator role", e1.getMessage());

    verify(roleRepository, never()).deleteById(any());
  }

  @Test
  void removeRoleNonExistentRoleForUser() {
    when(roleService.findById(any())).thenReturn(Optional.empty());
    Exception e4 =
        assertThrows(InvalidRoleDetailsException.class, () -> roleService.removeRole(1L, 1L));

    assertEquals("Role with given details not found", e4.getMessage());
  }

  @Test
  void removeRole() {
    Role roleToDelete = new Role(u1, i1, Role.RoleType.VOTER);
    when(roleRepository.findById(any())).thenReturn(Optional.of(roleToDelete));
    ArgumentCaptor<RoleId> argumentCaptor = ArgumentCaptor.forClass(RoleId.class);

    roleService.removeRole(1L, 1L);

    verify(roleRepository, times(1)).deleteById(argumentCaptor.capture());
    RoleId deletedRole = argumentCaptor.getValue();

    assertEquals(roleToDelete.getId(), deletedRole);
  }

  @Test
  void setRoleInvalidParameters() {
    assertThrows(ConstraintViolationException.class, () -> roleService.setRole(null, null, null));
    assertThrows(ConstraintViolationException.class, () -> roleService.setRole(i1, null, null));
    assertThrows(ConstraintViolationException.class, () -> roleService.setRole(null, u1, null));
    assertThrows(
        ConstraintViolationException.class,
        () -> roleService.setRole(null, null, Role.RoleType.APPLICANT));
    assertThrows(ConstraintViolationException.class, () -> roleService.setRole(i1, u1, null));
    assertThrows(
        ConstraintViolationException.class,
        () -> roleService.setRole(null, u1, Role.RoleType.APPLICANT));
    assertThrows(
        ConstraintViolationException.class,
        () -> roleService.setRole(i1, null, Role.RoleType.APPLICANT));
  }

  @Test
  void setRoleExclusive() {
    assertDoesNotThrow(() -> roleService.setRole(i1, u1, Role.RoleType.APPLICANT));
  }

  @Test
  void setRoleNonExclusive() {
    i1.setExclusive(false);
    assertThrows(
        InvalidInterestDetailsException.class,
        () -> roleService.setRole(i1, u1, Role.RoleType.APPLICANT));
  }

  @Test
  void setRoleCreatorForExclusive() {
    i1.setExclusive(true);
    assertDoesNotThrow(() -> roleService.setRole(i1, u1, Role.RoleType.CREATOR));
  }
}
