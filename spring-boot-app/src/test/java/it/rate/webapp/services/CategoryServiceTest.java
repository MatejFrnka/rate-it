package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.Constraints;
import it.rate.webapp.models.Category;
import it.rate.webapp.repositories.CategoryRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoryServiceTest extends BaseTest {
  @MockBean CategoryRepository categoryRepository;
  @Autowired CategoryService categoryService;

  @Test
  void findMaxLimitByIdIn_TooManyCategoriesThrowsException() {
    int maxLimit = Constraints.MAX_CATEGORIES_PER_INTEREST;
    Set<Long> categoryIds = new HashSet<>();
    for (int i = 0; i < maxLimit + 1; i++) {
      categoryIds.add((long) i + 1);
    }
    when(categoryRepository.findAllByIdIn(any())).thenReturn(List.of(new Category()));

    assertThrows(
        ConstraintViolationException.class, () -> categoryService.findMaxLimitByIdIn(categoryIds));
  }
}
