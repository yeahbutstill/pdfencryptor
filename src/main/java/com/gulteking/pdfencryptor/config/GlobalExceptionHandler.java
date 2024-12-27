package com.gulteking.pdfencryptor.config;

import com.gulteking.pdfencryptor.exception.PdfException;
import com.gulteking.pdfencryptor.model.Response;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PdfException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response<Object>> handlePdfException(PdfException ex) {
    log.error("PdfException: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "PDF processing error");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response<Object>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    log.error("IllegalArgumentException: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Invalid input argument");
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Response<Object>> handleEntityNotFoundException(
      EntityNotFoundException ex) {
    log.warn("EntityNotFoundException: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Entity not found");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response<Object>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    log.warn("Validation Error: {}", errorMessage, ex);
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Validation error: " + errorMessage);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response<Object>> handleRuntimeException(RuntimeException ex) {
    log.error("RuntimeException: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response<Object>> handleGenericException(Exception ex) {
    log.error("Unhandled Exception: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Unhandled exception occurred");
  }

  private ResponseEntity<Response<Object>> buildErrorResponse(
      Exception ex, HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .message(message)
                .devMessage(ex.getMessage()) // Pesan untuk pengembang
                .status(status)
                .statusCode(status.value())
                .build());
  }
}
