package it.rate.webapp.exceptions.badrequest;

public class UserAlreadyExistsException extends BadRequestException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException() {
    super("Ambiguous user record in database");
  }

  public UserAlreadyExistsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
