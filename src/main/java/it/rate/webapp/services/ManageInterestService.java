package it.rate.webapp.services;

import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidRoleDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.internalerror.InternalErrorException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class ManageInterestService {
  private final InterestService interestService;
  private final RoleService roleService;
  private final UserService userService;

  public void removeRole(@NotNull Long interestId, @NotNull Long userId) {
    Role role =
        roleService
            .findById(new RoleId(userId, interestId))
            .orElseThrow(InvalidRoleDetailsException::new);

    if (role.getRole().equals(Role.RoleType.CREATOR)) {
      throw new InvalidRoleDetailsException("Cannot remove creator role");
    }
    RoleId roleId = new RoleId(role.getId().getUserId(), role.getId().getInterestId());
    roleService.deleteByRoleId(roleId);
  }

  public Role adjustRole(
      @NotNull Long interestId, @NotNull Long userId, @NotNull Role.RoleType roleType) {

    Role role =
        roleService
            .findById(new RoleId(userId, interestId))
            .orElseThrow(InvalidRoleDetailsException::new);
    role.setRole(roleType);
    return roleService.save(role);
  }

  public Role inviteUser(
      @NotNull Long interestId,
      @NotBlank @Pattern(regexp = "\\b(?:username|email)\\b") String inviteBy,
      @NotBlank String user,
      @NotNull Role.RoleType roleType) {

    Interest interest =
        interestService
            .findById(interestId)
            .orElseThrow(InvalidInterestDetailsException::new);

    AppUser appUser;
    if (inviteBy.equals("username")) {
      appUser =
          userService.findByUsernameIgnoreCase(user).orElseThrow(InvalidUserDetailsException::new);
    } else if (inviteBy.equals("email")) {
      appUser =
          userService.findByEmailIgnoreCase(user).orElseThrow(InvalidUserDetailsException::new);
    } else {
      throw new InternalErrorException("Invalid inviteBy parameter");
    }

    Role role = new Role(appUser, interest, roleType);
    return roleService.save(role);
  }
}
