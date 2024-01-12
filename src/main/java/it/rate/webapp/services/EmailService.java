package it.rate.webapp.services;

import it.rate.webapp.dtos.EmailMessageDTO;

public interface EmailService {
  void sendEmail(EmailMessageDTO emailMessage);
}
