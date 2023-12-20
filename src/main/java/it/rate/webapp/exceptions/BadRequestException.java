package it.rate.webapp.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class BadRequestException extends Exception {
  public BadRequestException(String message) {
    super(message);
  }
}
