package com.gulteking.pdfencryptor.controller;

import com.gulteking.pdfencryptor.model.Response;
import com.gulteking.pdfencryptor.model.UserTaxResponse;
import com.gulteking.pdfencryptor.model.UserTradeConfoResponse;
import com.gulteking.pdfencryptor.model.UserTradeLoanResponse;
import com.gulteking.pdfencryptor.service.impl.PdfImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class PdfEncryptionController {

  private final PdfImpl pdf;

  @PostMapping(
      path = "/tradeconfo",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response<UserTradeConfoResponse>> createTradeConfo(
      @RequestParam("file") MultipartFile file,
      @NotBlank @RequestParam("password") String depositoNumber) {

    log.info("Processing TradeConfo request");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            Response.<UserTradeConfoResponse>builder()
                .timeStamp(LocalDateTime.now())
                .data(Collections.singletonList(pdf.generatePasswordPdfConfo(file, depositoNumber)))
                .message("TradeConfo Created Successfully")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build());
  }

  @PostMapping(
      path = "/tradeloan",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response<UserTradeLoanResponse>> createTradeLoan(
      @RequestParam("file") MultipartFile file, @NotBlank @RequestParam("password") String cif) {

    log.info("Processing TradeLoan request");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            Response.<UserTradeLoanResponse>builder()
                .timeStamp(LocalDateTime.now())
                .data(Collections.singletonList(pdf.generatePasswordPdfLoan(file, cif)))
                .message("TradeLoan Created Successfully")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build());
  }

  @PostMapping(
      path = "/tax",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response<UserTaxResponse>> createTax(
      @RequestParam("file") MultipartFile file, @NotBlank @RequestParam("password") String npwp) {

    log.info("Processing Tax request");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            Response.<UserTaxResponse>builder()
                .timeStamp(LocalDateTime.now())
                .data(Collections.singletonList(pdf.generatePasswordPdfTax(file, npwp)))
                .message("Tax Created Successfully")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build());
  }
}
