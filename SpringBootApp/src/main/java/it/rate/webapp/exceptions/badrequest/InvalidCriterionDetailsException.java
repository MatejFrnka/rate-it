package it.rate.webapp.exceptions.badrequest;

public class InvalidCriterionDetailsException extends BadRequestException {
  public InvalidCriterionDetailsException(String message) {
    super(message);
  }

  public InvalidCriterionDetailsException() {
    super("Criterion with given details not found");
  }

  public InvalidCriterionDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
