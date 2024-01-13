package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.EmailMessageDTO;
import it.rate.webapp.services.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailController {

  private EmailService emailService;

  @PostMapping("/scripts/email-test")
  public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailMessageDTO emailMessageDTO) {
    emailService.sendEmail(emailMessageDTO);
    return ResponseEntity.ok("Sent");
  }
}
