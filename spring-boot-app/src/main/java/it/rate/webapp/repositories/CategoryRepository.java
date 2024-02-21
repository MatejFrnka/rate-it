package it.rate.webapp.repositories;

import it.rate.webapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  public List<Category> findAllByIdIn(Set<Long> ids);
}
