package dev.rodrigovaamonde.keleatechnicaltest.driving.controllers.error;

import dev.rodrigovaamonde.keleatechnicaltest.application.exceptions.PriceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentTypeMismatchException typeMismatchException;

    @Mock
    private ConstraintViolationException constraintViolationException;

    @Mock
    private ConstraintViolation<Object> constraintViolation;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    void handlePriceNotFound_ShouldReturnNotFoundResponse() {
        // Given
        var exception = new PriceNotFoundException("Price not found for given criteria");

        // When
        var response = globalExceptionHandler.handlePriceNotFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Price Not Found");
        assertThat(response.getBody().message()).isEqualTo("Price not found for given criteria");
        assertThat(response.getBody().path()).isEqualTo("/test-uri");
    }

    @Test
    void handleConstraintViolation_ShouldReturnBadRequestResponse() {
        // Given
        when(constraintViolation.getMessage()).thenReturn("Invalid value");
        when(constraintViolationException.getConstraintViolations()).thenReturn(Set.of(constraintViolation));

        // When
        var response = globalExceptionHandler.handleConstraintViolation(constraintViolationException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Validation Error");
        assertThat(response.getBody().message()).isEqualTo("Invalid value");
        assertThat(response.getBody().path()).isEqualTo("/test-uri");
    }

    @Test
    void handleTypeMismatch_WithRequiredType_ShouldReturnBadRequestResponse() {
        // Given
        when(typeMismatchException.getName()).thenReturn("paramName");
        when(typeMismatchException.getRequiredType()).thenReturn((Class) Integer.class);

        // When
        var response = globalExceptionHandler.handleTypeMismatch(typeMismatchException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Invalid Parameter Type");
        assertThat(response.getBody().message()).isEqualTo("Parameter 'paramName' must be of type Integer");
        assertThat(response.getBody().path()).isEqualTo("/test-uri");
    }

    @Test
    void handleTypeMismatch_WithNullRequiredType_ShouldReturnBadRequestResponse() {
        // Given - Esta es la rama que faltaba cubrir
        when(typeMismatchException.getName()).thenReturn("paramName");
        when(typeMismatchException.getRequiredType()).thenReturn(null);

        // When
        var response = globalExceptionHandler.handleTypeMismatch(typeMismatchException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Invalid Parameter Type");
        assertThat(response.getBody().message()).isEqualTo("Parameter 'paramName' must be of type unknown");
        assertThat(response.getBody().path()).isEqualTo("/test-uri");
    }

    @Test
    void handleMissingParameter_ShouldReturnBadRequestResponse() {
        // Given
        var exception = new MissingServletRequestParameterException("requiredParam", "String");

        // When
        var response = globalExceptionHandler.handleMissingParameter(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Missing Required Parameter");
        assertThat(response.getBody().message()).isEqualTo("Required parameter 'requiredParam' is not present");
        assertThat(response.getBody().path()).isEqualTo("/test-uri");
    }
}
