package com.gulteking.pdfencryptor.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulteking.pdfencryptor.service.HmacService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class HmacServiceImpl implements HmacService {
    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final Random random = new Random();

    @Value("${app.secretkey}")
    private String secretKey;

    @Override
    public String generateHmac(String secretKey, String isoTimestamp, String requestBody)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalArgumentException {
        // Validate and parse ISO 8601 timestamp
        try {
            ZonedDateTime.parse(isoTimestamp, ISO_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO 8601 timestamp format: " + isoTimestamp);
        }

        // Combine timestamp and request body
        String data = isoTimestamp + requestBody;

        // Initialize HMAC-SHA512
        Mac mac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA512
        );
        mac.init(secretKeySpec);

        // Generate HMAC
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    @Override
    public Map<String, String> generateSignature(Map<String, Object> request) throws NoSuchAlgorithmException, InvalidKeyException {
        // Generate timestamp in +07:00 timezone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+07:00"));
        String timestamp = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // Generate random number (15 digits)
        long min = 100000000000000L;
        long max = 999999999999999L;
        long randomNumber = min + (this.random.nextLong() * (max - min));

        // Convert request body to JSON string and clean it
        ObjectMapper objectMapper = new ObjectMapper();
        String body;
        try {
            body = objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
        String replaceBody = body.replaceAll("[\\n\\r\\t\\s]", "");

        // Create component for signature
        String component = timestamp + replaceBody;

        // Generate HMAC-SHA512 signature
        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
        sha512Hmac.init(keySpec);
        byte[] hmacBytes = sha512Hmac.doFinal(component.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getEncoder().encodeToString(hmacBytes);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("request", body);
        response.put("randomNumber", String.valueOf(randomNumber));

        return response;
    }
}
