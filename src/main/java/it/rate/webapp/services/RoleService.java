package it.rate.webapp.services;

import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import it.rate.webapp.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
  private RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public void deleteByRoleId(RoleId roleId) {
    roleRepository.deleteById(roleId);
  }

  public Optional<Role> findById(RoleId roleId) {
    return roleRepository.findById(roleId);
  }

  public Role save(Role role) {
    return roleRepository.save(role);
  }
}
