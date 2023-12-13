package it.rate.webapp.services;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateInterestService {

    private InterestRepository interestRepository;
    private CriterionRepository criterionRepository;

    public Interest save(String name, String description, List<String> receivedCriteria) {
        List<Criterion> criteria = receivedCriteria.stream()
                .map(c -> Criterion.builder().name(c).build())
                .toList();

        criterionRepository.saveAll(criteria);

        Interest i = Interest.builder()
                .name(name)
                .description(description)
                .criteria(criteria)
                // todo: add user from session as admin to roles
                .build();


        return interestRepository.save(i);
    }
}