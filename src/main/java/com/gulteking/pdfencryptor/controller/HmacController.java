package com.gulteking.pdfencryptor.controller;

import com.gulteking.pdfencryptor.config.ApiKeyValidator;
import com.gulteking.pdfencryptor.exception.GlobalException;
import com.gulteking.pdfencryptor.service.HmacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/hmac")
@RequiredArgsConstructor
public class HmacController {

    private final HmacService hmacService;
    private final ApiKeyValidator apiKeyValidator;

    @PostMapping("/generate-signature")
    public ResponseEntity<Map<String, String>> generateSignature(
            @RequestHeader(value = "X-API-KEY") String apiKey,
            @RequestBody Map<String, Object> request) {
        log.info("Received request to generate signature with API key: {}", apiKey);
        try {
            // Validate API key
            apiKeyValidator.validateApiKey(apiKey);

            // Generate signature
            Map<String, String> response = hmacService.generateSignature(request);
            log.info("Successfully generated signature for request: {}", request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | SecurityException e) {
            log.error("Validation error: {}", e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Error generating signature", e);
            throw new GlobalException("Failed to generate signature: " + e.getMessage());
        }
    }
}