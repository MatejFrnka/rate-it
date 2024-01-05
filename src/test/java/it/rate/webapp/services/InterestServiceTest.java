package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
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

  @Autowired InterestService interestService;

  @Test
  void saveInterestWithNewCriteria() {
    List<String> newCriteriaNames = getNewCriteriaNames();

    List<Criterion> updatedCriteria = getNewCriteria();

    Interest interest = Interest.builder().id(1L).criteria(getOldCriteria()).build();

    when(interestRepository.getReferenceById(anyLong())).thenReturn(interest);
    when(interestRepository.save(any())).thenReturn(Interest.builder().criteria(updatedCriteria).build());
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
    when(interestRepository.save(any())).thenReturn(Interest.builder().criteria(updatedCriteria).build());
    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

    Interest savedInterest = interestService.saveEditedInterest(interest, newCriteriaNames);

    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c2", interest.getId());

    assertSame(savedInterest.getCriteria(), updatedCriteria);
  }

//  @Test
//  void saveNewInterest() {
//    when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//
//    Interest savedInterest = interestService.save(i);
//
//    assertEquals(criteria.size(), savedCriteria.size());
//    assertSame(savedInterest.getCriteria(), savedCriteria);
//
//    verify(interestRepository, times(1)).save(same(i));
//  }

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
