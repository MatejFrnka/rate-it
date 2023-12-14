package it.rate.webapp.services;

import it.rate.webapp.dtos.InterestSuggestionDto;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private RoleRepository roleRepository;
  private CriterionRepository criterionRepository;

  public Optional<Interest> findInterestById(Long id) {
    return interestRepository.findById(id);
  }

  public void vote() {
    // todo: add logic based on what will be received from html
  }

  public void setApplicantRole(Long interestId) {
    roleRepository.save(new Role(/*logged user, findInterestById(interestId), APPLICANT*/));
    // todo: add logged user to method logic
  }

  public List<Interest> findAllInterests() {
    return interestRepository.findAllSortByLikes();
  }

  public List<Interest> findInterestsByName(String query) {
    return interestRepository.findAllByNameSortByLikes(query);
  }

  public List<InterestSuggestionDto> getAllSuggestionDtos() {

    return findAllInterests().stream().map(InterestSuggestionDto::new).collect(Collectors.toList());
  }

  public List<Interest> getLikedInterests(String loggedUser) {
    return interestRepository.findAllByLikes_AppUser_Email(loggedUser);
  }

  public Interest saveEditedInterest(Interest interest, List<String> criteriaNames) {
    List<String> oldCriteriaNames =
        interestRepository.getReferenceById(interest.getId()).getCriteria().stream()
            .map(Criterion::getName)
            .toList();

    List<Criterion> newCriteria =
        criteriaNames.stream()
            .filter(name -> !oldCriteriaNames.contains(name))
            .map(name -> Criterion.builder().name(name).build())
            .toList();

    for (String name : oldCriteriaNames) {
      if (!criteriaNames.contains(name)) {
        criterionRepository.deleteByNameAndInterestId(name, interest.getId());
      }
    }

    criterionRepository.saveAll(newCriteria);
    newCriteria.forEach(c -> c.setInterest(interest));

    return interestRepository.save(interest);
  }
}
