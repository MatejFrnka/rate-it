package it.rate.webapp.exceptions.notfound;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlaceNotFoundException extends ResourceNotFoundException {
    public PlaceNotFoundException(String message) {
        super(message);
    }
    public PlaceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
