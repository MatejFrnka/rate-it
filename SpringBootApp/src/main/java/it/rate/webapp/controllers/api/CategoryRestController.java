package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.CategoryDTO;
import it.rate.webapp.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryRestController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<?> getAllCategories() {
    List<CategoryDTO> categories =
        categoryService.findAll().stream().map(CategoryDTO::new).toList();
    return ResponseEntity.ok().body(categories);
  }
}
