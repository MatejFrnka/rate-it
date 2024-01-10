package it.rate.webapp.services;

import it.rate.webapp.dtos.EmailMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Override
  public void sendEmail(EmailMessageDTO emailMessage) {
    SimpleMailMessage smm = new SimpleMailMessage();
    smm.setFrom("ratespot.email@centrum.cz");
    smm.setTo(emailMessage.getTo());
    smm.setSubject(emailMessage.getSubject());
    smm.setText(emailMessage.getText());

    this.mailSender.send(smm);

  }
}
