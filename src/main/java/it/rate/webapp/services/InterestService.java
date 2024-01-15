package it.rate.webapp.services;

import it.rate.webapp.dtos.InterestSuggestionDTO;
import it.rate.webapp.dtos.InterestUserDTO;
import it.rate.webapp.dtos.LikedInterestsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private RoleRepository roleRepository;
  private CriterionRepository criterionRepository;
  private UserRepository userRepository;

  public Optional<Interest> findInterestById(Long id) {
    return interestRepository.findById(id);
  }

  public Interest getById(Long id) {
    return interestRepository.getReferenceById(id);
  }

  public void setApplicantRole(Long interestId) {
    Interest interest =
        interestRepository.findById(interestId).orElseThrow(InvalidInterestDetailsException::new);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    roleRepository.save(new Role(currentUser, interest, Role.RoleType.APPLICANT));
  }

  public List<Interest> findAllInterests() {
    return interestRepository.findAllSortByLikes();
  }

  public List<InterestSuggestionDTO> getAllSuggestionDtos() {
    return findAllInterests().stream().map(InterestSuggestionDTO::new).collect(Collectors.toList());
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

  public List<LikedInterestsDTO> getLikedInterestsDTOS(@NotBlank String loggedUser) {
    return interestRepository.findAllByLikes_AppUser_Email(loggedUser).stream()
        .sorted(Comparator.comparing(i -> i.getName().toLowerCase()))
        .map(LikedInterestsDTO::new)
        .collect(Collectors.toList());
  }

  public Interest save(Interest interest) {
    return interestRepository.save(interest);
  }

  public List<InterestUserDTO> getUsersDTO(Interest interest, @NotNull Role.RoleType role) {

    return interest.getRoles().stream()
        .filter(r -> r.getRole().equals(role))
        .map(InterestUserDTO::new)
        .sorted(Comparator.comparing(dto -> dto.userName().toLowerCase()))
        .collect(Collectors.toList());
  }
}
