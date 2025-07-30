package com.gulteking.pdfencryptor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyValidator {

    @Value("${api.key}")
    private String validApiKey;

    public void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is missing");
        }

        if (!apiKey.equals(validApiKey)) {
            throw new SecurityException("Invalid API key");
        }
    }
}
