package com.gulteking.pdfencryptor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ExceptionModel> handleInternalException(InternalException ex, WebRequest request) {
        ExceptionModel error = ExceptionModel.builder()
                .errorMessage(ex.getMessage())
                .details(ex.getCause() != null ? ex.getCause().getMessage() : "No additional details")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionModel> handleGlobalException(GlobalException ex, WebRequest request) {
        ExceptionModel error = ExceptionModel.builder()
                .errorMessage(ex.getMessage())
                .details("Invalid input provided")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionModel> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ExceptionModel error = ExceptionModel.builder()
                .errorMessage(ExceptionMessages.INVALID_ARGUMENT)
                .details(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}