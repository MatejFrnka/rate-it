package it.rate.webapp.exceptions.badrequest;

public class PasswordMismatchException extends BadRequestException {
  public PasswordMismatchException(String message) {
    super(message);
  }

  public PasswordMismatchException() {
    super("Passwords do not match. Please try again.");
  }

  public PasswordMismatchException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
