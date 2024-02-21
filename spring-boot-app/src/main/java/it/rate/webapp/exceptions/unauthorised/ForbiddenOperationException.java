package it.rate.webapp.exceptions.unauthorised;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenOperationException extends AccessDeniedException {
  public ForbiddenOperationException(String message) {
    super(message);
  }

  public ForbiddenOperationException() {
    super("Forbidden operation");
  }

  public ForbiddenOperationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
