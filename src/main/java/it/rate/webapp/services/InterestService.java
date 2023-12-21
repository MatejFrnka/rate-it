package it.rate.webapp.services;

import it.rate.webapp.dtos.InterestSuggestionDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private RoleRepository roleRepository;
  private CriterionRepository criterionRepository;
  private LikeRepository likeRepository;
  private UserRepository userRepository;

  public Optional<Interest> findInterestById(Long id) {
    return interestRepository.findById(id);
  }

  public boolean isLiked(Long userId, Long interestId) {
    return likeRepository.existsById(new LikeId(userId, interestId));
  }

  public void changeLikeValue(Long interestId, String vote) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    if (vote.equals("like")) {
      likeRepository.save(new Like(currentUser, interestRepository.getReferenceById(interestId)));
    } else {
      likeRepository.deleteById(new LikeId(currentUser.getId(), interestId));
    }
  }

  public void setApplicantRole(Interest interest) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    roleRepository.save(new Role(currentUser, interest, Role.RoleType.APPLICANT));
  }

  public List<Interest> findAllInterests() {
    return interestRepository.findAllSortByLikes();
  }

  public List<Interest> findInterestsByName(String query) {
    return interestRepository.findAllByNameSortByLikes(query);
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

  public Interest save(Interest interest) {
    return interestRepository.save(interest);
  }
}
