package it.rate.webapp.config.security;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class UpdateSecurityContextAspect {
  private final UserService userService;

  @Before("@annotation(UpdateSecurityContext)")
  public void updateContext() {
    Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
    if (oldAuthentication.getPrincipal() == null
        || oldAuthentication.getPrincipal().equals("anonymousUser")) {
      return;
    }

    AppUser user = userService.getByEmail(oldAuthentication.getName());
    List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
    updatedAuthorities.add(new SimpleGrantedAuthority(user.getServerRole().name()));

    user.getRoles().stream()
        .map(
            r ->
                new SimpleGrantedAuthority(
                    String.format("ROLE_%s_%d", r.getRole().name(), r.getId().getInterestId())))
        .forEach(updatedAuthorities::add);

    Authentication updatedAuthentication =
        new UsernamePasswordAuthenticationToken(
            oldAuthentication.getPrincipal(),
            oldAuthentication.getCredentials(),
            updatedAuthorities);

    SecurityContext newContext = SecurityContextHolder.createEmptyContext();
    newContext.setAuthentication(updatedAuthentication);
    SecurityContextHolder.setContext(newContext);
  }
}
