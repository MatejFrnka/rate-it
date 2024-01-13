package it.rate.webapp.exceptions.api;


public class InvalidApiResponseException extends RuntimeException {
  public InvalidApiResponseException(String message) {
    super(message);
  }

  public InvalidApiResponseException() {
    super("Bad Gateway : Invalid API Response");
  }

  public InvalidApiResponseException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
