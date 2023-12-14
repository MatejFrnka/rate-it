package it.rate.webapp.services;

import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingService {
    private RatingRepository ratingRepository;
}
