package it.rate.webapp.services;

import it.rate.webapp.models.RoleId;
import it.rate.webapp.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void deleteByRoleId(RoleId roleId) {
        roleRepository.deleteById(roleId);
    }
}
