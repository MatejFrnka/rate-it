package it.rate.webapp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                        "/scripts/**",
                        "/",
                        "/interests/{id}",
                        "/interests/{id}/map",
                        "/interests/{interestId}/places/{placeId}",
                        "/users/signup",
                        "/users/login",
                        "/api/v1/interests/suggestions",
                        "/api/v1/interests/{id}/places",
                        "/api/v1/images/{id}")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/users/login").defaultSuccessUrl("/").permitAll())
        .logout(logout -> logout.logoutUrl("/users/logout").logoutSuccessUrl("/").permitAll())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
