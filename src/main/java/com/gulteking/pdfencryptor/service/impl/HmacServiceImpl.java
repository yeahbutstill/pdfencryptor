package com.gulteking.pdfencryptor.service.impl;

import com.gulteking.pdfencryptor.service.HmacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class HmacServiceImpl implements HmacService {
    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public String generateHmac(String secretKey, String isoTimestamp, String requestBody)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalArgumentException {
        // Validate inputs
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be null or empty");
        }
        if (requestBody == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        // Validate and parse ISO 8601 timestamp
        try {
            ZonedDateTime requestTime = ZonedDateTime.parse(isoTimestamp, ISO_FORMATTER);
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            if (Duration.between(requestTime, now).abs().toMinutes() > 2) {
                throw new IllegalArgumentException("Timestamp di luar waktu yang diterima");
            }
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
}