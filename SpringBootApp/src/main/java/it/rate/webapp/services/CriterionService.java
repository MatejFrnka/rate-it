package it.rate.webapp.services;

import it.rate.webapp.config.Constraints;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.CriterionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class CriterionService {
  private final CriterionRepository criterionRepository;

  public void saveAll(
      @Valid Interest interest,
      @NotEmpty Set<@NotBlank @Length(max = Constraints.MAX_NAME_LENGTH) String> criteriaNames) {
    List<Criterion> criteria =
        criteriaNames.stream()
            .map(c -> Criterion.builder().name(c).interest(interest).build())
            .toList();
    criterionRepository.saveAll(criteria);
  }

  @Transactional
  public void updateAll(
      @Valid Interest interest,
      @NotEmpty Set<@NotBlank @Length(max = Constraints.MAX_NAME_LENGTH) String> criteriaNames) {

    List<Criterion> oldCriteria = criterionRepository.findAllByInterest(interest);

    markDeletedIfNeeded(oldCriteria, criteriaNames);
    markUndeletedIfNeeded(oldCriteria, criteriaNames);
    addNewCriteriaIfNotExist(oldCriteria, criteriaNames, interest);
  }

  private void markDeletedIfNeeded(List<Criterion> oldCriteria, Set<String> criteriaNames) {
    oldCriteria.stream()
        .filter(criterion -> !criteriaNames.contains(criterion.getName()) && !criterion.isDeleted())
        .forEach(
            criterion -> {
              criterion.setDeleted(true);
              criterionRepository.save(criterion);
            });
  }

  private void markUndeletedIfNeeded(List<Criterion> oldCriteria, Set<String> criteriaNames) {
    oldCriteria.stream()
        .filter(criterion -> criteriaNames.contains(criterion.getName()) && criterion.isDeleted())
        .forEach(
            criterion -> {
              criterion.setDeleted(false);
              criterionRepository.save(criterion);
            });
  }

  private void addNewCriteriaIfNotExist(
      List<Criterion> oldCriteria, Set<String> criteriaNames, Interest interest) {
    Set<String> oldCriteriaNames =
        oldCriteria.stream().map(Criterion::getName).collect(Collectors.toSet());

    criteriaNames.stream()
        .filter(name -> !oldCriteriaNames.contains(name))
        .map(name -> Criterion.builder().name(name).interest(interest).build())
        .forEach(criterionRepository::save);
  }
}
