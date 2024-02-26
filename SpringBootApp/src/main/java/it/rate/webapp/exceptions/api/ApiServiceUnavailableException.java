package it.rate.webapp.exceptions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ApiServiceUnavailableException extends RuntimeException {
  public ApiServiceUnavailableException(String message) {
    super(message);
  }

  public ApiServiceUnavailableException() {
    super("Service Unavailable : Third Party API is unavailable");
  }

  public ApiServiceUnavailableException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
