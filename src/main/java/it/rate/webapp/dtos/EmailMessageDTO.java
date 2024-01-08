package it.rate.webapp.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailMessageDTO {
  String to;
  String subject;
  String message;
}
