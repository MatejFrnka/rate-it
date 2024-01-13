package it.rate.webapp.exceptions.badrequest;

public class InvalidIntererestDetailsException extends BadRequestException {
  public InvalidIntererestDetailsException(String message) {
    super(message);
  }

  public InvalidIntererestDetailsException() {
    super("Interest with given details not found in database");
  }

  public InvalidIntererestDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
