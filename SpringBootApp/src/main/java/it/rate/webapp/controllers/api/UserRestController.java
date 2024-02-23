package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.AppUserDTO;
import it.rate.webapp.dtos.FollowDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.unauthorised.ForbiddenOperationException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
  private final UserService userService;

  @PostMapping("/{userId}/follow")
  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  public ResponseEntity<?> followUser(
      @PathVariable Long userId, @RequestBody FollowDTO follow, Principal principal) {

    AppUser loggedUser = userService.getByEmail(principal.getName());
    Optional<AppUser> userToFollow = userService.findById(userId);

    if (userToFollow.isEmpty()) {
      return ResponseEntity.badRequest().body("User does not exist");
    }

    try {
      userService.follow(loggedUser, userToFollow.get(), follow.follow());
      return ResponseEntity.ok().body(follow);
    } catch (BadRequestException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/{userId}/editBio")
  @PreAuthorize("@permissionService.canEditUser(#editedUser.id())")
  public ResponseEntity<?> editBio(@RequestBody AppUserDTO editedUser) {
    try {
      userService.editUser(editedUser);
      return ResponseEntity.ok().build();
    } catch (ForbiddenOperationException e) {
      return ResponseEntity.status(403).body(e.getMessage());
    }
  }
}
