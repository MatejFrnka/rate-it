package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.models.RoleId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ManageInterestService {
  private InterestService interestService;
  private RoleService roleService;
  private UserService userService;

  public ManageInterestService(
      InterestService interestService, RoleService roleService, UserService userService) {
    this.interestService = interestService;
    this.roleService = roleService;
    this.userService = userService;
  }

  public Map<String, List<AppUser>> getUsersByRole(Long interestId) {
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    Map<String, List<AppUser>> map = new HashMap<>();
    Interest interest = optInterest.get();
    interest
        .getRoles()
        .forEach(
            r -> {
              if (!map.containsKey(r.getRole().name())) {
                map.put(r.getRole().name(), new ArrayList<>());
              }
              map.get(r.getRole().name()).add(r.getAppUser());
            });
    return map;
  }

  public void removeRole(Long interestId, Long userId) {
    if (userId == null || interestId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameter");
    }

    Optional<Role> optRole = roleService.findByAppUserIdAndInterestId(userId, interestId);
    if (optRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
    }
    Role role = optRole.get();
    roleService.deleteByRoleId(new RoleId(role.getId().getUserId(), role.getId().getInterestId()));
  }

  public Role adjustRole(Long interestId, Long userId, Role.RoleType roleType) {
    if (userId == null || interestId == null || roleType == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameter");
    }
    Optional<Role> optRole = roleService.findByAppUserIdAndInterestId(userId, interestId);
    if (optRole.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
    }
    Role role = optRole.get();
    role.setRole(roleType);
    return roleService.save(role);
  }

  public Role createNewRole(Long interestId, Long userId, Role.RoleType roleType) {
    if (userId == null || interestId == null || roleType == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameter");
    }
    Optional<Interest> optInterest = interestService.findInterestById(interestId);
    Optional<AppUser> optUser = userService.findById(userId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    if (optUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    AppUser user = optUser.get();
    Interest interest = optInterest.get();
    Role role = new Role(user, interest, roleType);
    return roleService.save(role);
  }
}
