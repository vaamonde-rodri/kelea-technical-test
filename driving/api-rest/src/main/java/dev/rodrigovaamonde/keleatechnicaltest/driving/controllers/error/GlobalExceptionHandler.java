package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.error;

import dev.rodrigovaamonde.keleatechnicaltest.application.exceptions.PriceNotFoundException;
import dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePriceNotFound(
            PriceNotFoundException ex, HttpServletRequest request
    ) {
        var errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Price Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
