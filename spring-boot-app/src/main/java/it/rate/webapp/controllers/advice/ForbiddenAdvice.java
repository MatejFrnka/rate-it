package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ConditionalOnProperty(name = "advice.enabled", havingValue = "true")
@ControllerAdvice
@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ForbiddenAdvice {
  private final String clientMessage = "You don't have permissions to perform this action.";
  private final String simpleMessage = "Forbidden";
  private final int statusCode = HttpStatus.FORBIDDEN.value();
  private final Logger logger = LoggerFactory.getLogger(ForbiddenAdvice.class);

  @ExceptionHandler(AccessDeniedException.class)
  public ModelAndView handleForbiddenOperationException(AccessDeniedException e) {
    logger.error("Forbidden Operation Exception has occurred", e);
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
