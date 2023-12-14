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

  public ManageInterestService(InterestService interestService, RoleService roleService) {
    this.interestService = interestService;
    this.roleService = roleService;
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

  public void removeVoter(Long interestId, Long userId) {
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
}
