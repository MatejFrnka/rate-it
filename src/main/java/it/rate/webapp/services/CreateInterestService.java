package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.RoleRepository;
import it.rate.webapp.repositories.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class CreateInterestService {

  private InterestRepository interestRepository;
  private CriterionRepository criterionRepository;
  private UserRepository userRepository;
  private RoleRepository roleRepository;

  public Interest save(
      @NotBlank String name,
      @NotBlank String description,
      @NotEmpty List<@NotBlank String> receivedCriteria) {

    List<Criterion> criteria =
        receivedCriteria.stream().map(c -> Criterion.builder().name(c).build()).toList();

    criteria = criterionRepository.saveAll(criteria);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    Interest newInterest = Interest.builder().name(name).description(description).criteria(criteria).build();

    interestRepository.save(newInterest);
    criteria.forEach(c -> c.setInterest(newInterest));

    criterionRepository.saveAll(criteria);
    Role newRole = new Role(currentUser, newInterest, Role.RoleType.CREATOR);
    newInterest.setRoles(List.of(newRole));
    roleRepository.save(newRole);

    return newInterest;
  }
}
