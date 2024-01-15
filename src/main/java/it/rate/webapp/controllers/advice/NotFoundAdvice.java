package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.notfound.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ConditionalOnProperty(name = "advice.enabled", havingValue = "true")
@ControllerAdvice
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotFoundAdvice {
  private final String clientMessage = "Sorry, we couldn't quite find what you were looking for.";
  private final String simpleMessage = "Page not found";
  private final int statusCode = HttpStatus.NOT_FOUND.value();

  @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
  public ModelAndView handleResourceNotFoundException(Exception e) {
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
