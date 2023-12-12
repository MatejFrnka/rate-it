package it.rate.webapp.services;

import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private PlaceRepository placeRepository;

  public Optional<Interest> findInterestById(Long id) {
    return interestRepository.findById(id);
  }

  public void vote() {
    // todo: add logic based on what will be received from html
  }

  public void addApplicant() {
    // todo: add list of applicants to Interest model and add logged user into it
  }
}
