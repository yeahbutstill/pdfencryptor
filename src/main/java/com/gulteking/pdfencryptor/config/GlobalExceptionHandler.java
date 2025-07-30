package com.gulteking.pdfencryptor.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.ExceptionModel;
import com.gulteking.pdfencryptor.exception.InternalException;
import com.gulteking.pdfencryptor.exception.PdfException;
import com.gulteking.pdfencryptor.model.HmacResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(value = PdfException.class)
    public ResponseEntity<ExceptionModel> pdfException(PdfException exception) {
        log.error("PdfException", exception);
        return new ResponseEntity<>(toExceptionModel(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InternalException.class)
    public ResponseEntity<ExceptionModel> internalException(InternalException exception) {
        log.error("InternalException", exception);
        return new ResponseEntity<>(toExceptionModel(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionModel> unhandledException(Exception exception) {
        log.error("Unhandled Exception", exception);
        ExceptionModel exceptionModel = toExceptionModel(exception);
        exceptionModel.setErrorDescription(ExceptionMessages.UNHANDLED_EXCEPTION);
        if (exceptionModel.getInternalErrorDescription() != null) {
            exceptionModel.setInternalErrorDescription(ExceptionMessages.UNHANDLED_EXCEPTION);
        }
        return new ResponseEntity<>(exceptionModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private ExceptionModel toExceptionModel(Throwable exception) {
        ExceptionModel.ExceptionModelBuilder builder =
                ExceptionModel.builder()
                        .error(exception.getClass().getSimpleName())
                        .errorDescription(exception.getMessage());

        if (exception.getCause() != null) {
            builder
                    .internalError(exception.getCause().getClass().getSimpleName())
                    .internalErrorDescription(exception.getCause().getMessage());
        }
        return builder.build();
    }
}
