package it.rate.webapp.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorAdvice {
  private final String clientMessage = "Something went wrong. Please try again later.";
  private final String simpleMessage = "Internal server error";
  private final int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

  //  @ExceptionHandler(Exception.class)
  //  public ModelAndView unhandledExceptions(Exception e) {
  //    return new ModelAndView(
  //        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  //  }
}
