package it.rate.webapp.services;

import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class ManageInterestService {
  private final InterestService interestService;
  private final RoleService roleService;
  private final UserService userService;

  public void removeRole(Long interestId, Long userId) {
    if (userId == null || interestId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameter");
    }

    Optional<Role> optRole = roleService.findByAppUserIdAndInterestId(userId, interestId);
    if (optRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
    }
    Role role = optRole.get();
    if (role.getRole().equals(Role.RoleType.CREATOR)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove creator role");
    }
    RoleId roleId = new RoleId(role.getId().getUserId(), role.getId().getInterestId());
    roleService.deleteByRoleId(roleId);
    // todo : CHANGES WONT TAKE EFFECT UNTIL USER RELOGS // NEED TO MANIPULATE SESSION
  }

  public Role adjustRole(Long interestId, Long userId, Role.RoleType roleType)
      throws BadRequestException {
    if (userId == null || interestId == null || roleType == null) {
      throw new BadRequestException("Missing parameter");
    }
    Optional<Role> optRole = roleService.findByAppUserIdAndInterestId(userId, interestId);
    if (optRole.isEmpty()) {
      throw new BadRequestException("Role not found");
    }
    Role role = optRole.get();
    role.setRole(roleType);
    return roleService.save(role);
    // todo : CHANGES WONT TAKE EFFECT UNTIL USER RELOGS // NEED TO MANIPULATE SESSION
  }

  public Role inviteUser(Long interestId, String inviteBy, String user, Role.RoleType roleType) {
    if (inviteBy == null || user == null || interestId == null || roleType == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameter");
    }
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    Optional<AppUser> optUser;
    if (inviteBy.equals("email")) {
      optUser = userService.findByEmailIgnoreCase(user);
    } else if (inviteBy.equals("username")) {
      optUser = userService.findByUsernameIgnoreCase(user);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameter");
    }
    Interest interest =
        optInterest.orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found"));
    AppUser appUser =
        optUser.orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Role role = new Role(appUser, interest, roleType);
    return roleService.save(role);
    // todo : CHANGES WONT TAKE EFFECT UNTIL USER RELOGS // NEED TO MANIPULATE SESSION
  }
}
