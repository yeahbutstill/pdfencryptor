package com.gulteking.pdfencryptor.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulteking.pdfencryptor.model.HmacResponse;
import com.gulteking.pdfencryptor.service.HmacService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hmac")
@RequiredArgsConstructor
public class HmacController {

    private final HmacService hmacService;
    private final ObjectMapper objectMapper;

    @PostMapping("/generate")
    public ResponseEntity<HmacResponse> generateHmac(
            @RequestHeader("X-Secret-Key") String secretKey,
            @RequestBody String requestBody) throws Exception {
        // Parse JSON request body
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String timestamp = jsonNode.get("timestamp").asText();
        String body = jsonNode.get("requestBody").asText();

        // Generate HMAC
        String hmac = hmacService.generateHmac(secretKey, timestamp, body);
        HmacResponse response = new HmacResponse("success", hmac, timestamp, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-signature")
    public ResponseEntity<Map<String, String>> generateSignature(@RequestBody Map<String, Object> request) {
        try {
            Map<String, String> response = hmacService.generateSignature(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}