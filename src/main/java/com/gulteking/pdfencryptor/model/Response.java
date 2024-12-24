package com.gulteking.pdfencryptor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
// @ApiModel(description = "Standard API Response")
public class Response<T> {
  protected LocalDateTime timeStamp;
  protected Integer statusCode;
  protected HttpStatus status;
  protected String reason;
  protected String message;
  protected String devMessage; // Bisa digunakan untuk debug mode saja.
  protected List<T> data; // Type-safe untuk data.
}
