package com.gulteking.pdfencryptor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulteking.pdfencryptor.dto.HmacResponse;
import com.gulteking.pdfencryptor.service.HmacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/v1/hmac")
@RequiredArgsConstructor
public class HmacController {
    private final HmacService hmacService;
    //private final ApiKeyValidator apiKeyValidator;
    private final ObjectMapper objectMapper;

    @PostMapping("/generate")
    public ResponseEntity<HmacResponse> generateHmac(
            //@RequestHeader(value = "X-API-KEY") String apiKey,
            @RequestHeader("X-SECRET-KEY") String secretKey,
            @RequestHeader("BDI-Timestamp") String BDITimestamp,
            @RequestBody JsonNode requestBodyJson) {
        try {
            //apiKeyValidator.validateApiKey(apiKey);

            // Konversi JSON ke string untuk HMAC
            String requestBody = objectMapper.writeValueAsString(requestBodyJson);

            // Generate timestamp in ISO 8601 format (WIB)
            log.info(BDITimestamp);
            log.info("Process");

            // Generate HMAC using the JSON string
            String hmac = hmacService.generateHmac(secretKey, BDITimestamp, requestBody);
            log.info(BDITimestamp);

            // Return response with provided and generated values
            return ResponseEntity.ok(new HmacResponse("success", hmac, BDITimestamp, null, requestBody));
        } catch (IllegalArgumentException e) {
            log.warn("Permintaan tidak valid: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new HmacResponse("error", null, null, e.getMessage(), null));
        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            log.error("Gagal menghasilkan HMAC atau JSON: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HmacResponse("error", null, null, "Gagal menghasilkan HMAC atau JSON", null));
        }
    }
}