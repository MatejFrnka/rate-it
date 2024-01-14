package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import it.rate.webapp.exceptions.api.InvalidApiResponseException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

@ConditionalOnProperty(name = "advice.enabled", havingValue = "true")
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiRequestAdvice {
  private String clientMessage;
  private String simpleMessage;
  private int statusCode;

  @ExceptionHandler(InvalidApiResponseException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ModelAndView handleHttpClientErrorException(HttpClientErrorException e) {
    statusCode = HttpStatus.BAD_GATEWAY.value();
    simpleMessage = "Bad Gateway";
    clientMessage =
        "Sorry, we received an invalid response from a third party service. Please try again later.";
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }

  @ExceptionHandler(value = ApiServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ModelAndView handleServiceUnavailableException(Exception e) {
    statusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
    simpleMessage = "Service Unavailable";
    clientMessage = "Sorry, a third party service is unavailable. Please try again later.";
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
