package it.rate.webapp.services;

import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private VoteRepository voteRepository;

  public List<Interest> findAll(String query) {
    //todo: sort by total rating
    if (query == null || query.isBlank()) {
      return interestRepository.findAll();
    }
    return interestRepository.findAllByNameContaining(query);
  }

  public List<String> findAllNames() { //placeholder for javascript
    return interestRepository.findAll().stream()
            .map(Interest::getName)
            .toList();
  }

  public Interest save(Interest interest) {
    return interestRepository.save(interest);
  }

  public Optional<Interest> findById(Long id) {
    return interestRepository.findById(id);
  }

  public void vote() {
    //todo: add logic based on what will be received from html
  }

  public void addApplicant() {
    //todo: add list of applicants to Interest model and add logged user into it
  }
}
