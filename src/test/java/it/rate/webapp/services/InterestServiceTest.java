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

    List<Criterion> criteria = new ArrayList<>();
    Criterion c1;
    Criterion c2;
    Interest i;

    @BeforeEach
    void createCriteria() {
        c1 = Criterion.builder().name("c1").build();
        c2 = Criterion.builder().name("c2").build();
        criteria.add(c1);
        criteria.add(c2);
        criterionRepository.saveAll(criteria);

        i = Interest.builder().criteria(criteria).build();
    }

    @Test
    void saveInterestWithNewCriteria() {
        List<Criterion> updatedCriteria = new ArrayList<>();
        Criterion c3 = new Criterion();
        Criterion c4 = new Criterion();
        updatedCriteria.add(c3);
        updatedCriteria.add(c4);

        when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        criterionRepository.deleteByNameAndInterestId(c1.getName(), i.getId());
        criterionRepository.deleteByNameAndInterestId(c2.getName(), i.getId());
        criterionRepository.saveAll(updatedCriteria);

        i.setCriteria(updatedCriteria);
        Interest savedInterest = interestService.save(i);

        assertSame(savedInterest.getCriteria(), updatedCriteria);
        assertFalse(criterionRepository.findAll().contains(c1));
        assertFalse(criterionRepository.findAll().contains(c2));
    }

    @Test
    void saveInterestWithChangedCriteria() {
        List<Criterion> updatedCriteria = new ArrayList<>();
        Criterion c3 = new Criterion();
        Criterion c4 = new Criterion();
        updatedCriteria.add(c2);
        updatedCriteria.add(c3);
        updatedCriteria.add(c4);

        when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));


        criterionRepository.deleteByNameAndInterestId(c1.getName(), i.getId());
        criterionRepository.saveAll(updatedCriteria);

        i.setCriteria(updatedCriteria);
        Interest editedInterest = interestRepository.save(i);

        assertEquals(3, editedInterest.getCriteria().size());
        assertSame(editedInterest.getCriteria(), updatedCriteria);
        assertFalse(criterionRepository.findAll().contains(c1));
    }

    @Test
    void saveNewInterest() {
        when(interestRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(criterionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        List<Criterion> savedCriteria = criterionRepository.saveAll(criteria);
        Interest savedInterest = interestService.save(i);

        assertEquals(criteria.size(), savedCriteria.size());
        assertSame(savedInterest.getCriteria(), savedCriteria);

        verify(interestRepository, times(1)).save(same(i));
    }
}