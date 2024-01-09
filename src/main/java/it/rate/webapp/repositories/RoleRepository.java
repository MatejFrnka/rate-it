package it.rate.webapp.repositories;

import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
