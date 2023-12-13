package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateInterestService {

    private InterestRepository interestRepository;
    private CriterionRepository criterionRepository;
    private UserRepository userRepository;

    public Interest save(String name, String description, List<String> receivedCriteria) {
        List<Criterion> criteria = receivedCriteria.stream()
                .map(c -> Criterion.builder().name(c).build())
                .toList();

        criterionRepository.saveAll(criteria);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<AppUser> currentUser = userRepository.findByEmail(authentication.getName());

        if (currentUser.isEmpty()) {
            throw new RuntimeException(); // todo: probably something more specific
        }

        Interest i = Interest.builder()
            .name(name)
            .description(description)
            .criteria(criteria)
            .build();

        i.getRoles().add(new Role(currentUser.get(), i, Role.RoleType.ADMIN));
        // todo: ADMIN to be changed to CREATOR after merge

        return interestRepository.save(i);
    }
}