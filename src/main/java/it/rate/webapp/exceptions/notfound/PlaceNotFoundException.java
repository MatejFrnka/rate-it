package it.rate.webapp.exceptions.notfound;

public class PlaceNotFoundException extends ResourceNotFoundException {
  public PlaceNotFoundException() {
    super("Place was not found in database");
  }

  public PlaceNotFoundException(String message) {
    super(message);
  }

  public PlaceNotFoundException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
