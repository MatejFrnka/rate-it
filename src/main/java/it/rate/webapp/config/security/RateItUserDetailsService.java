package it.rate.webapp.config.security;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RateItUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  public RateItUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    String username = null;
    String password = null;
    List<GrantedAuthority> authorities = null;
    Optional<AppUser> optAppUser = users.findByEmail(email);

    if (optAppUser.isEmpty()) {
      throw new UsernameNotFoundException("User details not found for : " + email);
    } else {
      AppUser appUser = optAppUser.get();
      username = appUser.getEmail();
      password = appUser.getPassword();
      authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("USER"));
    }
    return new User(username, password, authorities);
  }
}
