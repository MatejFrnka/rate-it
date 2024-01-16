package it.rate.webapp.controllers.advice;

import it.rate.webapp.dtos.EmailMessageDTO;
import it.rate.webapp.dtos.ErrorResponseDTO;
import it.rate.webapp.exceptions.internalerror.InternalErrorException;
import it.rate.webapp.services.EmailService;
import it.rate.webapp.services.UserService;
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
  private final UserService userService;
  private final String devEmail = "rate.spot.dev@gmail.com";
  private final String clientMessage =
      "Something went wrong. Our developers were notified. Please try again later.";
  private final String simpleMessage = "Internal server error";
  private final int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();


  @ExceptionHandler({InternalErrorException.class, Exception.class})
  public ModelAndView unhandledExceptions(Exception e) {
//    emailService.sendEmail(buildExceptionReport(devEmail, e));
    return new ModelAndView(
        "error/page", "error", new ErrorResponseDTO(statusCode, simpleMessage, clientMessage));
  }

  private EmailMessageDTO buildExceptionReport(String email, Exception e) {
    StringBuilder stackTrace = new StringBuilder();
    Arrays.stream(e.getStackTrace())
        .forEach(line -> stackTrace.append(line.toString()).append("\n"));
    String user =
        userService.authenticatedUser() == null
            ? "Anonymous"
            : userService.authenticatedUser().getEmail();

    EmailMessageDTO report =
        new EmailMessageDTO(
            email,
            "Unexpected error with cause: " + e.getCause(),
            String.format(
                """
                                Unexpected error thrown : %s\s

                                Triggered by: %s\s

                                Timestamp: %s\s

                                Stacktrace :\s

                                %s""",
                e.getMessage(), user, ZonedDateTime.now((ZoneId.of("UTC"))), stackTrace));
    return report;
  }
}
