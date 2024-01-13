package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.notfound.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundAdvice {
  private final String clientMessage = "Sorry, we couldn't quite find what you were looking for.";
  private final String simpleMessage = "Page not found";
  private final int statusCode = HttpStatus.NOT_FOUND.value();

  @ExceptionHandler(ResourceNotFoundException.class)
  public ModelAndView handleResourceNotFoundException(ResourceNotFoundException e) {
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
