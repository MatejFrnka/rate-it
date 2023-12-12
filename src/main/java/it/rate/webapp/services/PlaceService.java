package it.rate.webapp.services;

import it.rate.webapp.repositories.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaceService {

    private PlaceRepository placeRepository;
}
