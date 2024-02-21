package it.rate.webapp.exceptions.api;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
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
