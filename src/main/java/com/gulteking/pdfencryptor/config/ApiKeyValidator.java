package com.gulteking.pdfencryptor.config;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
@Component
public class ApiKeyValidator {

    private final String validApiKey;

    public ApiKeyValidator(@Value("${api.key:default-api-key}") String validApiKey) {
        this.validApiKey = Objects.requireNonNull(validApiKey, "API key configuration cannot be null");
        if (validApiKey.isBlank()) {
            log.error("API key configuration is empty or invalid");
            throw new IllegalStateException("API key configuration must not be empty");
        }
    }

    public void validateApiKey(@NotNull String apiKey) {
        if (apiKey.isBlank()) {
            log.warn("API key validation failed: missing or empty");
            throw new GlobalException(ExceptionMessages.INVALID_ARGUMENT);
        }

        if (!apiKey.equals(validApiKey)) {
            log.warn("API key validation failed: invalid key");
            throw new GlobalException(ExceptionMessages.SECURITY_VIOLATION);
        }

        log.debug("API key validated successfully");
    }
}