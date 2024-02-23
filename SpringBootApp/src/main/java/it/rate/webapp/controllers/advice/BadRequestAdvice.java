package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@ConditionalOnProperty(name = "advice.enabled", havingValue = "true")
@ControllerAdvice
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BadRequestAdvice {
  private final String clientMessage =
      "Sorry, your request was invalid. Please check the details and try again.";
  private final String simpleMessage = "Bad request";
  private final int statusCode = HttpStatus.BAD_REQUEST.value();
  private final Logger logger = LoggerFactory.getLogger(BadRequestAdvice.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String errorMessage =
        e.getBindingResult().getFieldError().getField()
            + ": "
            + e.getBindingResult().getFieldError().getDefaultMessage();
    logger.error("Method Argument Not Valid Exception has occurred", e);
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, errorMessage));
  }

  @ExceptionHandler({
    BadRequestException.class,
    ConstraintViolationException.class,
    MissingServletRequestParameterException.class,
    HttpRequestMethodNotSupportedException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ModelAndView handleConstraintViolationException(Exception e) {
    logger.error("Constraint Violation Exception has occurred", e);
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
