package it.rate.webapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailMessageDTO {
  String to;
  String subject;
  String text;
}
