package it.rate.webapp.controllers.api;

import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interests")
public class InterestRestController {
    private final InterestService interestService;

    @GetMapping("/suggestions")
    public ResponseEntity<?> getAllSuggestions() {
        return ResponseEntity.ok().body(interestService.getAllSuggestionDtos());
    }
}
