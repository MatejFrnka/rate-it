package it.rate.webapp.services;

import it.rate.webapp.enums.InviteBy;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
import it.rate.webapp.exceptions.badrequest.UserAlreadyExistsException;
import it.rate.webapp.exceptions.internalerror.InternalErrorException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.RoleRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class ManageInterestService {
  private final RoleRepository roleRepository;
  private final UserService userService;

  public Role inviteUser(
      @Valid @NotNull Interest interest,
      @NotNull InviteBy inviteBy,
      @NotBlank String user,
      @NotNull Role.RoleType roleType) {

    AppUser appUser;
    if (inviteBy.equals(InviteBy.USERNAME)) {
      appUser =
          userService.findByUsernameIgnoreCase(user).orElseThrow(InvalidUserDetailsException::new);
    } else if (inviteBy.equals(InviteBy.EMAIL)) {
      appUser =
          userService.findByEmailIgnoreCase(user).orElseThrow(InvalidUserDetailsException::new);
    } else {
      throw new InternalErrorException("Invalid invitedBy parameter");
    }

    interest.getRoles().stream()
        .filter(
            r -> r.getAppUser().equals(appUser) && !r.getRoleType().equals(Role.RoleType.APPLICANT))
        .findFirst()
        .ifPresent(
            r -> {
              throw new UserAlreadyExistsException("User is already a member of this interest");
            });

    Role role = new Role(appUser, interest, roleType);
    return roleRepository.save(role);
  }

  public InviteBy mapInvite(@NotBlank String inviteBy) {
    try {
      return InviteBy.valueOf(inviteBy);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid invite parameter");
    }
  }
}
