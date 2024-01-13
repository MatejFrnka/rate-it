package it.rate.webapp.exceptions.notfound;

public class InterestNotFoundException extends ResourceNotFoundException {
  public InterestNotFoundException() {
    super("Interest was not found in database");
  }

  public InterestNotFoundException(String message) {
    super(message);
  }

  public InterestNotFoundException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
