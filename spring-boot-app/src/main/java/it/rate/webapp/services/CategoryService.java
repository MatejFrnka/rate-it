package it.rate.webapp.services;

import it.rate.webapp.config.Constraints;
import it.rate.webapp.models.Category;
import it.rate.webapp.repositories.CategoryRepository;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Service
@Validated
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public List<Category> findAllByIdIn(Set<Long> ids) {
    return categoryRepository.findAllByIdIn(ids);
  }

  public List<Category> findMaxLimitByIdIn(
      @Size(max = Constraints.MAX_CATEGORIES_PER_INTEREST) Set<Long> ids) {
    return findAllByIdIn(ids);
  }
}
