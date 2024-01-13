package it.rate.webapp.exceptions.badrequest;

public class InvalidPlaceDetailsException extends BadRequestException {
  public InvalidPlaceDetailsException(String message) {
    super(message);
  }

  public InvalidPlaceDetailsException() {
    super("Place with given details not found in database");
  }

  public InvalidPlaceDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
