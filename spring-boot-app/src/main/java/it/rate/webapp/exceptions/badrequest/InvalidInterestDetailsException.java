package it.rate.webapp.exceptions.badrequest;

public class InvalidInterestDetailsException extends BadRequestException {
  public InvalidInterestDetailsException(String message) {
    super(message);
  }

  public InvalidInterestDetailsException() {
    super("Interest with given details not found");
  }

  public InvalidInterestDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
