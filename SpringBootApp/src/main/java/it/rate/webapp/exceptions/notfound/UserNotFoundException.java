package it.rate.webapp.exceptions.notfound;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException() {
    super("User was not found in database");
  }

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
