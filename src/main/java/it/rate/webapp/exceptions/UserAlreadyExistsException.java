package it.rate.webapp.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}