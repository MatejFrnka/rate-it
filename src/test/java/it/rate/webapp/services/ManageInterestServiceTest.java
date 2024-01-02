package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import it.rate.webapp.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManageInterestServiceTest extends BaseTest {

  @MockBean InterestService interestService;
  @MockBean RoleService roleService;
  @MockBean RoleRepository roleRepository;
  @MockBean UserService userService;

  @Autowired ManageInterestService manageInterestService;

  Interest i1;
  AppUser u1;

  @BeforeEach
  void setUp() {
    u1 = AppUser.builder().username("Lojza").id(1L).build();

    AppUser u2 = AppUser.builder().username("Alfonz").id(2L).build();

    AppUser u3 = AppUser.builder().username("Karel").id(3L).build();

    i1 = new Interest();

    Role r1 = new Role(u1, i1, Role.RoleType.CREATOR);
    Role r2 = new Role(u2, i1, Role.RoleType.VOTER);
    Role r3 = new Role(u3, i1, Role.RoleType.APPLICANT);

    i1.setRoles(List.of(r1, r2, r3));
  }

  @Test
  void getUsersByRoleValidInterest() throws BadRequestException {
    when(interestService.findInterestById(any())).thenReturn(Optional.of(i1));
    Map<String, List<AppUser>> result = manageInterestService.getUsersByRole(i1.getId());

    assertEquals("Lojza", result.get("CREATOR").get(0).getUsername());
    assertEquals("Alfonz", result.get("VOTER").get(0).getUsername());
    assertEquals("Karel", result.get("APPLICANT").get(0).getUsername());
  }

  @Test
  void getUsersByRoleInvalidInterest() throws BadRequestException {
    when(interestService.findInterestById(any())).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> manageInterestService.getUsersByRole(1L));
  }

  @Test
  void removeRoleInvalidParams() {
    Exception e1 =
        assertThrows(
            ResponseStatusException.class, () -> manageInterestService.removeRole(null, null));
    Exception e2 =
        assertThrows(
            ResponseStatusException.class, () -> manageInterestService.removeRole(null, 1L));
    Exception e3 =
        assertThrows(
            ResponseStatusException.class, () -> manageInterestService.removeRole(1L, null));

    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e1.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e2.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e3.getMessage());
  }

  @Test
  void removeRoleRemoveCreatorRoleThrowsException() {
    Role roleToDelete = new Role(u1, i1, Role.RoleType.CREATOR);
    when(roleService.findByAppUserIdAndInterestId(any(), any()))
        .thenReturn(Optional.of(roleToDelete));

    Exception e1 = assertThrows(ResponseStatusException.class, () -> manageInterestService.removeRole(1L, 1L));
    assertEquals("400 BAD_REQUEST \"Cannot remove creator role\"", e1.getMessage());
  }

  @Test
  void removeRoleNonExistentRoleForUser() {
    when(roleService.findByAppUserIdAndInterestId(any(), any())).thenReturn(Optional.empty());
    Exception e4 =
        assertThrows(ResponseStatusException.class, () -> manageInterestService.removeRole(1L, 1L));

    assertEquals("404 NOT_FOUND \"Role not found\"", e4.getMessage());
  }

  @Test
  void removeRole() {
    Role roleToDelete = new Role(u1, i1, Role.RoleType.VOTER);
    when(roleService.findByAppUserIdAndInterestId(any(), any()))
        .thenReturn(Optional.of(roleToDelete));
    ArgumentCaptor<RoleId> argumentCaptor = ArgumentCaptor.forClass(RoleId.class);

    manageInterestService.removeRole(1L, 1L);
    roleRepository.deleteById(roleToDelete.getId());

    verify(roleService, times(1)).deleteByRoleId(roleToDelete.getId());
    verify(roleRepository).deleteById(argumentCaptor.capture());
    verify(roleRepository, times(1)).deleteById(same(roleToDelete.getId()));

    RoleId deletedRole = argumentCaptor.getValue();
    assertSame(roleToDelete.getId(), deletedRole);
  }

  @Test
  void adjustRoleInvalidParameters() throws BadRequestException {
    Exception e1 =
        assertThrows(
            BadRequestException.class, () -> manageInterestService.adjustRole(null, null, null));
    Exception e2 =
        assertThrows(
            BadRequestException.class, () -> manageInterestService.adjustRole(1L, null, null));
    Exception e3 =
        assertThrows(
            BadRequestException.class, () -> manageInterestService.adjustRole(null, 1L, null));
    Exception e4 =
        assertThrows(
            BadRequestException.class,
            () -> manageInterestService.adjustRole(null, null, Role.RoleType.APPLICANT));
    Exception e5 =
        assertThrows(
            BadRequestException.class, () -> manageInterestService.adjustRole(1L, 1L, null));
    Exception e6 =
        assertThrows(
            BadRequestException.class,
            () -> manageInterestService.adjustRole(null, 1L, Role.RoleType.APPLICANT));
    Exception e7 =
        assertThrows(
            BadRequestException.class,
            () -> manageInterestService.adjustRole(1L, null, Role.RoleType.APPLICANT));

    assertEquals("Missing parameter", e1.getMessage());
    assertEquals("Missing parameter", e2.getMessage());
    assertEquals("Missing parameter", e3.getMessage());
    assertEquals("Missing parameter", e4.getMessage());
    assertEquals("Missing parameter", e5.getMessage());
    assertEquals("Missing parameter", e6.getMessage());
    assertEquals("Missing parameter", e7.getMessage());
  }

  @Test
  void adjustRoleNonExistentRoleForUser() {
    when(roleService.findByAppUserIdAndInterestId(any(), any())).thenReturn(Optional.empty());
    Exception e =
        assertThrows(
            BadRequestException.class,
            () -> manageInterestService.adjustRole(1L, 1L, Role.RoleType.APPLICANT));

    assertEquals("Role not found", e.getMessage());
  }

  @Test
  void adjustRole() throws BadRequestException {
    Role roleToAdjust = new Role(u1, i1, Role.RoleType.APPLICANT);
    Role.RoleType roleToApply = Role.RoleType.VOTER;
    when(roleService.findByAppUserIdAndInterestId(any(), any()))
        .thenReturn(Optional.of(roleToAdjust));

    manageInterestService.adjustRole(1L, 1L, roleToApply);

    verify(roleService, times(1)).save(same(roleToAdjust));
    assertEquals(roleToApply, roleToAdjust.getRole());
  }

  @Test
  void createNewRoleInvalidParameters() {
    Exception e1 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(null, null, null));
    Exception e2 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(1L, null, null));
    Exception e3 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(null, 1L, null));
    Exception e4 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(null, null, Role.RoleType.APPLICANT));
    Exception e5 =
        assertThrows(
            ResponseStatusException.class, () -> manageInterestService.createNewRole(1L, 1L, null));
    Exception e6 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(null, 1L, Role.RoleType.APPLICANT));
    Exception e7 =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(1L, null, Role.RoleType.APPLICANT));

    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e1.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e2.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e3.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e4.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e5.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e6.getMessage());
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e7.getMessage());
  }

  @Test
  void createNewRoleNonExistentInterest() {
    when(interestService.findInterestById(any())).thenReturn(Optional.empty());
    Exception e =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(1L, 1L, Role.RoleType.APPLICANT));

    assertEquals("404 NOT_FOUND \"Interest not found\"", e.getMessage());
  }

  @Test
  void createNewRoleNonExistentUser() {
    when(interestService.findInterestById(any())).thenReturn(Optional.of(i1));
    when(userService.findById(any())).thenReturn(Optional.empty());
    Exception e =
        assertThrows(
            ResponseStatusException.class,
            () -> manageInterestService.createNewRole(1L, 1L, Role.RoleType.APPLICANT));

    assertEquals("404 NOT_FOUND \"User not found\"", e.getMessage());
  }

  @Test
  void createNewRoleReturnsNewRole() {
    AppUser userWithoutRole = new AppUser();
    Role.RoleType roleToCreate = Role.RoleType.VOTER;
    when(interestService.findInterestById(any())).thenReturn(Optional.of(i1));
    when(userService.findById(any())).thenReturn(Optional.of(userWithoutRole));
    when(roleService.save(any())).then(i -> roleRepository.save(i.getArgument(0)));
    when(roleRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    Role newRole = manageInterestService.createNewRole(1L, 1L, roleToCreate);

    verify(roleService, times(1)).save(newRole);
    verify(roleRepository, times(1)).save(newRole);
    assertEquals(roleToCreate, newRole.getRole());
    assertEquals(userWithoutRole, newRole.getAppUser());
  }
}
