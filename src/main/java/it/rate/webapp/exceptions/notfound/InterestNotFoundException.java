package it.rate.webapp.exceptions.notfound;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InterestNotFoundException extends ResourceNotFoundException {
    public InterestNotFoundException(String message) {
        super(message);
    }
    public InterestNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
