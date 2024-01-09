package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InterestServiceTest extends BaseTest {

  @MockBean InterestRepository interestRepository;
  @MockBean CriterionRepository criterionRepository;
  @MockBean UserRepository userRepository;
  @MockBean LikeRepository likeRepository;

  @Autowired InterestService interestService;

  @Test
  void saveInterestWithNewCriteria() {
    List<String> newCriteriaNames = getNewCriteriaNames();

    List<Criterion> updatedCriteria = getNewCriteria();

    Interest interest = Interest.builder().id(1L).criteria(getOldCriteria()).build();

    when(interestRepository.getReferenceById(anyLong())).thenReturn(interest);
    when(interestRepository.save(any()))
        .thenReturn(Interest.builder().criteria(updatedCriteria).build());
    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

    Interest savedInterest = interestService.saveEditedInterest(interest, newCriteriaNames);

    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c1", interest.getId());
    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c2", interest.getId());

    assertSame(savedInterest.getCriteria(), updatedCriteria);
  }

  @Test
  void saveInterestWithChangedCriteria() {
    List<String> newCriteriaNames = getNewCriteriaNames();

    List<Criterion> updatedCriteria = new ArrayList<>(getNewCriteria());
    updatedCriteria.add(Criterion.builder().name("c1").build());

    Interest interest = Interest.builder().id(1L).criteria(getOldCriteria()).build();

    when(interestRepository.getReferenceById(anyLong())).thenReturn(interest);
    when(interestRepository.save(any()))
        .thenReturn(Interest.builder().criteria(updatedCriteria).build());
    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

    Interest savedInterest = interestService.saveEditedInterest(interest, newCriteriaNames);

    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c2", interest.getId());

    assertSame(savedInterest.getCriteria(), updatedCriteria);
  }

  @Test
  void saveInterestWithoutChangingCriteria() {
    List<String> criteriaNames = getOldCriteria().stream().map(Criterion::getName).toList();

    Interest interest = Interest.builder().id(1L).criteria(getOldCriteria()).build();

    when(interestRepository.getReferenceById(anyLong())).thenReturn(interest);
    when(interestRepository.save(any())).thenReturn(interest);
    when(criterionRepository.saveAll(any())).thenReturn(getOldCriteria());

    Interest savedInterest = interestService.saveEditedInterest(interest, criteriaNames);

    verify(criterionRepository, times(0)).deleteByNameAndInterestId(any(), any());

    assertSame(savedInterest.getCriteria(), interest.getCriteria());
  }

  private List<Criterion> getOldCriteria() {
    return List.of(Criterion.builder().name("c1").build(), Criterion.builder().name("c2").build());
  }

  private List<String> getNewCriteriaNames() {
    return List.of("c3", "c4");
  }

  private List<Criterion> getNewCriteria() {
    return List.of(Criterion.builder().name("c3").build(), Criterion.builder().name("c4").build());
  }
}
