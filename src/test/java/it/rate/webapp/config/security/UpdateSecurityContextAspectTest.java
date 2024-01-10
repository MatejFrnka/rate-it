package it.rate.webapp.config.security;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.UserService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UpdateSecurityContextAspectTest extends BaseTest {

  @Autowired UpdateSecurityContextAspect updateSecurityContextAspect;
  @MockBean UserService userService;

  @Test
  void updateContextRemovesVoterRole() {
    // Arrange User
    AppUser user = AppUser.builder().serverRole(ServerRole.USER).build();
    List<Role> roles =
        new ArrayList<>(
            List.of(new Role(user, Interest.builder().id(1L).build(), Role.RoleType.VOTER)));
    user.setRoles(roles);

    // Arrange Security Context
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getServerRole().name()));
    user.getRoles().stream()
        .map(
            r ->
                new SimpleGrantedAuthority(
                    String.format("ROLE_%s_%d", r.getRole().name(), r.getId().getInterestId())))
        .forEach(authorities::add);

    Authentication auth =
        new UsernamePasswordAuthenticationToken("test@test.cz", null, authorities);
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userService.getByEmail(any())).thenReturn(user);

    // Act
    user.getRoles()
        .removeIf(
            r -> r.getRole().equals(Role.RoleType.VOTER) && r.getInterest().getId().equals(1L));
    updateSecurityContextAspect.updateContext(null, null);

    // Assert
    assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    assertEquals(
        "USER",
        SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .toArray()[0]
            .toString());
  }
}
