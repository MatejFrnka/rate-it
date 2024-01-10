package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.security.UpdateSecurityContextAspect;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class PermissionServiceTest extends BaseTest {
  @MockBean PlaceRepository placeRepository;
  @MockBean InterestRepository interestRepository;
  @MockBean UserRepository userRepository;
  @MockBean RoleRepository roleRepository;
  @MockBean UpdateSecurityContextAspect updateSecurityContextAspect;

  @Autowired PermissionService permissionService;

  @Test
  void hasRatingPermission() {
    Interest i1 = Interest.builder().exclusive(true).id(1L).build();
    Interest i2 = Interest.builder().exclusive(false).id(2L).build();

    AppUser u1 = AppUser.builder().id(1L).build();
    AppUser u2 = AppUser.builder().id(2L).build();
    AppUser u3 = AppUser.builder().id(3L).build();
    AppUser u4 = AppUser.builder().id(4L).build();

    Role r1 = new Role(u1, i1, Role.RoleType.CREATOR);
    Role r2 = new Role(u2, i1, Role.RoleType.VOTER);
    Role r3 = new Role(u3, i1, Role.RoleType.APPLICANT);

    when(roleRepository.findById(new RoleId(u1.getId(), i1.getId()))).thenReturn(Optional.of(r1));
    when(roleRepository.findById(new RoleId(u2.getId(), i1.getId()))).thenReturn(Optional.of(r2));
    when(roleRepository.findById(new RoleId(u3.getId(), i1.getId()))).thenReturn(Optional.of(r3));
    when(roleRepository.findById(new RoleId(u4.getId(), i1.getId()))).thenReturn(Optional.empty());

    assertTrue(permissionService.hasRatingPermission(u1, i1));
    assertTrue(permissionService.hasRatingPermission(u2, i1));
    assertFalse(permissionService.hasRatingPermission(u3, i1));
    assertFalse(permissionService.hasRatingPermission(u4, i1));
    assertTrue(permissionService.hasRatingPermission(u1, i2));
    assertTrue(permissionService.hasRatingPermission(u2, i2));
    assertTrue(permissionService.hasRatingPermission(u3, i2));
    assertTrue(permissionService.hasRatingPermission(u4, i2));
  }
}
