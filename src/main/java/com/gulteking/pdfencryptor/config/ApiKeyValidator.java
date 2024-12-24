package com.gulteking.pdfencryptor.config;

import com.gulteking.pdfencryptor.repository.ApiKeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiKeyValidator {

  private final ApiKeyRepository apiKeyRepository;

  public ApiKeyValidator(ApiKeyRepository apiKeyRepository) {
    this.apiKeyRepository = apiKeyRepository;
  }

  @Cacheable(value = "apiKeys", key = "#apiKey", unless = "#result == false")
  public boolean isValid(String apiKey) {
    log.debug("Checking validity of API Key: {}", apiKey);
    try {
      boolean isValid = apiKeyRepository.existsByApiKey(apiKey);
      logValidationResult(apiKey, isValid);
      return isValid;
    } catch (Exception e) {
      log.error("Error validating API Key: {}. Cause: {}", apiKey, e.getMessage(), e);
      return false;
    }
  }

  private void logValidationResult(String apiKey, boolean isValid) {
    if (isValid) {
      log.info("API Key validated successfully: {}", apiKey);
    } else {
      log.warn("Invalid API Key provided: {}", apiKey);
    }
  }
}
