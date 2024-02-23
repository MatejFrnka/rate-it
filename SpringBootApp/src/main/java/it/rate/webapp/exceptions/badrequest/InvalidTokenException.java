package it.rate.webapp.exceptions.badrequest;

public class InvalidTokenException extends BadRequestException {
  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException() {
    super("Invalid token");
  }

  public InvalidTokenException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
