package it.rate.webapp.exceptions.badrequest;

public class InvalidUserDetailsException extends BadRequestException {
  public InvalidUserDetailsException(String message) {
    super(message);
  }

  public InvalidUserDetailsException() {
    super("User with given details not found");
  }

  public InvalidUserDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
