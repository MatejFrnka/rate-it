package it.rate.webapp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    http.authorizeHttpRequests(
            authRequests ->
                authRequests
                    .requestMatchers(
                        "/h2-console/**",
                        "/styles/**",
                        "/icons/**",
                        "/images/**",
                        "/scripts/**",
                        "/about/**",
                        "/",
                        "/interests/{id}",
                        "/interests/{id}/map",
                        "/interests/{interestId}/places/{placeId}",
                        "/signup",
                        "/login",
                        "/reset",
                        "/users/{username}",
                        "/users/{username}/interests/{interestId}",
                        "/api/v1/interests/suggestions",
                        "/api/v1/interests/{id}/places",
                        "/api/v1/interests/{id}",
                        "/api/v1/images/**",
                        "/api/v1/categories",
                        "/api/v1/emails/contact-us")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/").permitAll())
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
