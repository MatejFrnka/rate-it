package it.rate.webapp.config.security;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UpdateSecurityContextAspectTest extends BaseTest {

  @Autowired UpdateSecurityContextAspect updateSecurityContextAspect;
  @MockBean UserService userService;

  @Test
  void updateContextRemovesAuthorityFromContext() {
    // Arrange User
    AppUser user = AppUser.builder().serverRole(ServerRole.USER).build();
    Interest interest = Interest.builder().id(1L).build();
    Role.RoleType roleToRemove = Role.RoleType.VOTER;
    user.getRoles().add(new Role(user, interest, roleToRemove));

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
            r ->
                r.getRole().equals(roleToRemove)
                    && r.getInterest().getId().equals(interest.getId()));
    updateSecurityContextAspect.updateContext();

    // Assert
    assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    assertFalse(
        SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_VOTER_1")));
  }

  @Test
  void updateContextAddsAuthorityToContext() {
    // Arrange User
    AppUser user = AppUser.builder().serverRole(ServerRole.USER).build();
    Interest interest = Interest.builder().id(1L).build();
    Role.RoleType roleToAdd = Role.RoleType.VOTER;

    // Arrange Security Context
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getServerRole().name()));

    Authentication auth =
        new UsernamePasswordAuthenticationToken("test@test.cz", null, authorities);
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userService.getByEmail(any())).thenReturn(user);

    // Act
    user.getRoles().add(new Role(user, interest, roleToAdd));
    updateSecurityContextAspect.updateContext();

    // Assert
    assertEquals(2, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    assertTrue(
        SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_VOTER_1")));
  }
}
