package it.rate.webapp.services;

import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PlaceService {

  private PlaceRepository placeRepository;

  public Place save(Place place) {
    return placeRepository.save(place);
  }

  public Optional<Place> getById(Long id) {
      return placeRepository.findById(id);
  }

  
}
