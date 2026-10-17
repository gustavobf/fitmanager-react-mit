package com.gustavofigueiredo.fitmanager.config;

import com.gustavofigueiredo.fitmanager.dto.common.ApiError;
import com.gustavofigueiredo.fitmanager.exception.ApiException;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleApiException(ApiException exception) {
        return response(exception.getStatus(), exception.getCodigo(), exception.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .toList();
        return response(HttpStatus.BAD_REQUEST, "REQUISICAO_INVALIDA",
                "A requisição contém dados inválidos.", details);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public org.springframework.http.ResponseEntity<ApiError> handleInvalidRequest(Exception exception) {
        return response(HttpStatus.BAD_REQUEST, "REQUISICAO_INVALIDA",
                "A requisição contém dados inválidos.", List.of(exception.getMessage()));
    }

    private org.springframework.http.ResponseEntity<ApiError> response(
            HttpStatus status, String code, String message, List<String> details) {
        return org.springframework.http.ResponseEntity.status(status)
                .body(new ApiError(code, message, details, OffsetDateTime.now()));
    }
}
