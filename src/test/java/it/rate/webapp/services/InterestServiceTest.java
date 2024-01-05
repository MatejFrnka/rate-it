package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class InterestServiceTest extends BaseTest {

  @MockBean InterestRepository interestRepository;
  @MockBean CriterionRepository criterionRepository;

  @Autowired InterestService interestService;

  @Test
  void saveInterestWithNewCriteria() {
    List<String> updatedCriteriaStrings = List.of("c3", "c4");
    Criterion c3 = new Criterion();
    Criterion c4 = new Criterion();

    List<Criterion> updatedCriteria = new ArrayList<>();
    updatedCriteria.add(c3);
    updatedCriteria.add(c4);

    Interest interest = Interest.builder().id(1L).criteria(getOldCriteria()).build();

    when(interestRepository.getReferenceById(anyLong())).thenReturn(interest);
    when(interestRepository.save(any())).thenReturn(Interest.builder().criteria(updatedCriteria).build());
    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

    Interest savedInterest = interestService.saveEditedInterest(interest, updatedCriteriaStrings);

    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c1", interest.getId());
    verify(criterionRepository, times(1)).deleteByNameAndInterestId("c2", interest.getId());

    assertSame(savedInterest.getCriteria(), updatedCriteria);
  }

//  @Test
//  void saveInterestWithChangedCriteria() {
//    List<Criterion> updatedCriteria = new ArrayList<>();
//    Criterion c3 = new Criterion();
//    Criterion c4 = new Criterion();
//    updatedCriteria.add(c2);
//    updatedCriteria.add(c3);
//    updatedCriteria.add(c4);
//
//    when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));
//
//    criterionRepository.deleteByNameAndInterestId(c1.getName(), i.getId());
//    criterionRepository.saveAll(updatedCriteria);
//
//    i.setCriteria(updatedCriteria);
//    Interest editedInterest = interestRepository.save(i);
//
//    assertEquals(3, editedInterest.getCriteria().size());
//    assertSame(editedInterest.getCriteria(), updatedCriteria);
//    assertFalse(criterionRepository.findAll().contains(c1));
//  }
//
//  @Test
//  void saveNewInterest() {
//    when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//    when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));
//
//    List<Criterion> savedCriteria = criterionRepository.saveAll(criteria);
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
}
