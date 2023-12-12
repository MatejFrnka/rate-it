package it.rate.webapp.services;

import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.InterestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;

  public List<Interest> getAll(String query) {
    //todo: sort by total rating
    if (query == null || query.isBlank()) {
      return interestRepository.findAll();
    }
    return interestRepository.findAllByNameContaining(query);
  }

  public Optional<Interest> getById(Long id) {
    return interestRepository.findById(id);
  }
}
