package it.rate.webapp.services;

import it.rate.webapp.dtos.InterestSuggestionDto;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
  private final InterestRepository interestRepository;

  public List<Interest> findAllInterests() {
    return interestRepository.findAllSortByVoteValueDesc();
  }

  public List<Interest> findInterestsByName(String query) {
    return interestRepository.findAllByNameSortByVoteValueDesc(query);
  }

  public List<InterestSuggestionDto> getAllSuggestionDtos() {

    return findAllInterests().stream().map(InterestSuggestionDto::new).collect(Collectors.toList());
  }
}
