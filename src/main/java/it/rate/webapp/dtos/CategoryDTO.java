package it.rate.webapp.dtos;

import it.rate.webapp.models.Category;

public record CategoryDTO(Long id, String name) {
  public CategoryDTO(Category category) {
    this(category.getId(), category.getName());
  }
}
