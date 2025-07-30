package com.gulteking.pdfencryptor.controller.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.gulteking.pdfencryptor.model.HmacResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HmacResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        HmacResponse response = new HmacResponse("error", null, null, "Error generating HMAC: " + e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler({NoSuchAlgorithmException.class, InvalidKeyException.class})
    public ResponseEntity<HmacResponse> handleSecurityExceptions(Exception e) {
        HmacResponse response = new HmacResponse("error", null, null, "Error generating HMAC: " + e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler({JsonMappingException.class, com.fasterxml.jackson.core.JsonProcessingException.class})
    public ResponseEntity<HmacResponse> handleJsonProcessingException(Exception e) {
        HmacResponse response = new HmacResponse("error", null, null, "Invalid JSON format: " + e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HmacResponse> handleGenericException(Exception e) {
        HmacResponse response = new HmacResponse("error", null, null, "Unexpected error: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}