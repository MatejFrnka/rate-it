package it.rate.webapp.services;

import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidRoleDetailsException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import it.rate.webapp.repositories.RoleRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  public Optional<Role> findById(RoleId roleId) {
    return roleRepository.findById(roleId);
  }

  public Role save(@Valid Role role) {
    return roleRepository.save(role);
  }

  public void setRole(
          @NotNull @Valid Interest interest, @NotNull @Valid AppUser appUser, @NotNull Role.RoleType roleType) {
    if (interest.isExclusive() || roleType.equals(Role.RoleType.CREATOR)) {
      roleRepository.save(new Role(appUser, interest, roleType));
    } else {
      throw new InvalidInterestDetailsException("Cannot set role for non-exclusive interest");
    }
  }

  public void removeRole(@NotNull Long interestId, @NotNull Long userId) {
    Role role =
        roleRepository
            .findById(new RoleId(userId, interestId))
            .orElseThrow(InvalidRoleDetailsException::new);

    if (role.getRoleType().equals(Role.RoleType.CREATOR)) {
      throw new InvalidRoleDetailsException("Cannot remove creator role");
    }

    roleRepository.deleteById(role.getId());
  }

  public void removeAllVoterRoles(@NotNull Long interestId) {
    roleRepository.deleteAllByInterestIdAndRoleType(interestId, Role.RoleType.VOTER);
  }
}
