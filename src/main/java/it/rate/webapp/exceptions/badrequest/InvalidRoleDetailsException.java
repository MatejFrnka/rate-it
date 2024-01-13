package it.rate.webapp.exceptions.badrequest;

public class InvalidRoleDetailsException extends BadRequestException {
  public InvalidRoleDetailsException(String message) {
    super(message);
  }

  public InvalidRoleDetailsException() {
    super("Role with given details not found in database");
  }

  public InvalidRoleDetailsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
