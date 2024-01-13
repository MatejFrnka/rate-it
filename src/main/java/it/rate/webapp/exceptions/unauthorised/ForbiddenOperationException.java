package it.rate.webapp.exceptions.unauthorised;

import org.springframework.security.access.AccessDeniedException;

public class ForbiddenOperationException extends AccessDeniedException {
    public ForbiddenOperationException(String message) {
        super(message);
    }

    public ForbiddenOperationException() {
        super("Forbidden operation");
    }

    public ForbiddenOperationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
