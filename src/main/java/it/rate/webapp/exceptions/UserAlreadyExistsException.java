package it.rate.webapp.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends BadRequestException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
