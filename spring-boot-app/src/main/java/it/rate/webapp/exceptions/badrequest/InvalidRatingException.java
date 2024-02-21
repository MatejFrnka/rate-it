package it.rate.webapp.exceptions.badrequest;

public class InvalidRatingException extends BadRequestException {
  public InvalidRatingException(String message) {
    super(message);
  }

  public InvalidRatingException() {
    super("Invalid rating");
  }

  public InvalidRatingException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
