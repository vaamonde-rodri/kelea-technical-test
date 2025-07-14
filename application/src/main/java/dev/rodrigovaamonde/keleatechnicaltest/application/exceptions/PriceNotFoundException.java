package dev.rodrigovaamonde.keleatechnicaltest.application.exceptions;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
