package it.rate.webapp.repositories;

import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
  Optional<Role> findByAppUserIdAndInterestId(Long userId, Long interestId);
}
