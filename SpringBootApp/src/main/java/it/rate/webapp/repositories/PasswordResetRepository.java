package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
  Optional<PasswordReset> findByUser(AppUser user);

  Optional<PasswordReset> findByUser_Id(Long userId);
}
