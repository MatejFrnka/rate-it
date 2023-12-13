package it.rate.webapp.services;

import it.rate.webapp.dtos.InterestSuggestionDto;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private RoleRepository roleRepository;

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
    return interestRepository.findAllSortByVoteValueDesc();
  }

  public List<Interest> findInterestsByName(String query) {
    return interestRepository.findAllByNameSortByVoteValueDesc(query);
  }

  public List<InterestSuggestionDto> getAllSuggestionDtos() {

    return findAllInterests().stream().map(InterestSuggestionDto::new).collect(Collectors.toList());
  }
}
