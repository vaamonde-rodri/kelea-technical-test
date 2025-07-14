package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
