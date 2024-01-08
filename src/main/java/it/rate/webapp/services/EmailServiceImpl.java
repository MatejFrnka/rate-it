package it.rate.webapp.services;

import it.rate.webapp.dtos.EmailMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender emailSender;

  @Override
  public void sendEmail(EmailMessageDTO emailMessage) {}
}
