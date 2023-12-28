package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.InterestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ManageInterestServiceTest extends BaseTest {

  @MockBean Authentication authentication;
  @MockBean SecurityContext securityContext;
  @MockBean InterestService interestService;

  @Autowired ManageInterestService manageInterestService;

  Interest i1;
  AppUser u1;
  AppUser u2;
  AppUser u3;

  @BeforeEach
  void setUp() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("joe");

    SecurityContextHolder.setContext(securityContext);

    u1 = AppUser.builder().username("Lojza").id(1L).build();

    u2 = AppUser.builder().username("Alfonz").id(2L).build();

    u3 = AppUser.builder().username("Karel").id(3L).build();

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
    Exception e1 = assertThrows(ResponseStatusException.class, () -> manageInterestService.removeRole(null, null));
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e1.getMessage());

    Exception e2 = assertThrows(ResponseStatusException.class, () -> manageInterestService.removeRole(null, 1L));
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e2.getMessage());

    Exception e3 = assertThrows(ResponseStatusException.class, () -> manageInterestService.removeRole(1L, null));
    assertEquals("400 BAD_REQUEST \"Missing parameter\"", e3.getMessage());
  }



  @Test
  void adjustRole() {}

  @Test
  void createNewRole() {}
}
