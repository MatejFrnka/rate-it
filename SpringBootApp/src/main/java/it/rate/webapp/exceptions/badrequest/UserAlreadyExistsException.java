package it.rate.webapp.exceptions.badrequest;

public class UserAlreadyExistsException extends BadRequestException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException() {
    super("User with these details already exists");
  }

  public UserAlreadyExistsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
