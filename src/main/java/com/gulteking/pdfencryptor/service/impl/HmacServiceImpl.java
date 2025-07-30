package com.gulteking.pdfencryptor.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulteking.pdfencryptor.exception.GlobalException;
import com.gulteking.pdfencryptor.service.HmacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class HmacServiceImpl implements HmacService {

    private static final String HMAC_SHA512 = "HmacSHA512";
    private final Random random = new Random();
    private final ObjectMapper objectMapper;
    @Value("${app.secretkey}")
    private String secretKey;

    @Override
    public Map<String, String> generateSignature(Map<String, Object> request) {
        try {
            // Generate timestamp in +07:00 timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+07:00"));
            String timestamp = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            // Generate random number (15 digits)
            long min = 100000000000000L;
            long max = 999999999999999L;
            long randomNumber = min + (this.random.nextLong() * (max - min));

            // Convert request body to JSON string and clean it
            String body = objectMapper.writeValueAsString(request);
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
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error generating HMAC signature", e);
            throw new GlobalException("Failed to generate HMAC signature: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error serializing request body", e);
            throw new GlobalException("Failed to process request body: " + e.getMessage());
        }
    }
}
