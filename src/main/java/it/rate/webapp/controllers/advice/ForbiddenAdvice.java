package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenAdvice {
  private final String clientMessage = "You don't have permissions to perform this action.";
  private final String simpleMessage = "Forbidden";
  private final int statusCode = HttpStatus.FORBIDDEN.value();

  @ExceptionHandler(AccessDeniedException.class)
  public ModelAndView handleForbiddenOperationException(AccessDeniedException e) {
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
