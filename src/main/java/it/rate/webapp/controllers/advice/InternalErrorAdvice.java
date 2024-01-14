package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.EmailMessageDTO;
import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

@ConditionalOnProperty(name = "advice.enabled", havingValue = "true")
@ControllerAdvice
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorAdvice {
  private final EmailService emailService;
  private final String clientMessage =
      "Something went wrong. Our developers were notified. Please try again later.";
  private final String simpleMessage = "Internal server error";
  private final int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

  @ExceptionHandler(Exception.class)
  public ModelAndView unhandledExceptions(Exception e) {
    EmailMessageDTO report =
        new EmailMessageDTO(
            "rate.spot.dev@gmail.com",
            "Unexpected error with cause: " + e.getCause(),
            String.format(
                "Unexpected error thrown : %s \n\n" + "Timestamp: %s \n\n" + "Stacktrace : %s",
                e.getMessage(),
                ZonedDateTime.now((ZoneId.of("UTC"))),
                Arrays.toString(e.getStackTrace()).replaceAll("\n", "\n\n")));
    emailService.sendEmail(report);

    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }
}
