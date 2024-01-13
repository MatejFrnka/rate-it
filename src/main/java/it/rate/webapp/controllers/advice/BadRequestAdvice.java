package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestAdvice {
  private final String clientMessage =
      "Sorry, your request was invalid. Please check the details and try again.";
  private final String simpleMessage = "Bad request";
  private final int statusCode = HttpStatus.BAD_REQUEST.value();

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String errorMessage =
        e.getBindingResult().getFieldError().getField()
            + ": "
            + e.getBindingResult().getFieldError().getDefaultMessage();
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, errorMessage));
  }

  @ExceptionHandler(BadRequestException.class)
  public ModelAndView handleBadRequestException(BadRequestException e) {
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
