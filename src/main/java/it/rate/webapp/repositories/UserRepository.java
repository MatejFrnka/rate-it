package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
  Optional<AppUser> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  AppUser getByEmail(String email);
}
