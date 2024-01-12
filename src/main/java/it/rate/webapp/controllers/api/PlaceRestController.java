package it.rate.webapp.controllers.api;

import it.rate.webapp.models.Interest;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interests")
public class PlaceRestController {

    private final PlaceService placeService;
    private final InterestService interestService;

    @GetMapping("/{interestId}/places")
    public ResponseEntity<?> getAllPlaceInfoDTOs(@PathVariable Long interestId) {
        Interest interest = interestService.getById(interestId);
        return ResponseEntity.ok().body(placeService.getPlaceInfoDTOS(interest));
    }
}
