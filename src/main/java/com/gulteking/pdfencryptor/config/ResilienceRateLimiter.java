package com.gulteking.pdfencryptor.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResilienceRateLimiter {

  private final RateLimiter defaultRateLimiter;
  private final Map<String, RateLimiter> rateLimiterMap;

  public ResilienceRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
    this.defaultRateLimiter = rateLimiterRegistry.rateLimiter("default");
    this.rateLimiterMap = new HashMap<>();
    rateLimiterMap.put("Tax", rateLimiterRegistry.rateLimiter("user-specific"));
    rateLimiterMap.put("Loan", rateLimiterRegistry.rateLimiter("loan-specific"));
    rateLimiterMap.put("Confo", rateLimiterRegistry.rateLimiter("confo-specific"));
    // Tambahkan kategori lain jika diperlukan
  }

  public boolean isAllowed(String apiKey) {
    // Pilih RateLimiter berdasarkan prefix API Key
    RateLimiter rateLimiter = getRateLimiterForApiKey(apiKey);

    boolean allowed = rateLimiter.acquirePermission();
    if (!allowed) {
      log.warn(
          "Rate limit exceeded for API Key: {}. Next permission available after {} ms",
          apiKey,
          rateLimiter.reservePermission() / 1_000_000);
    } else {
      log.info("RateLimiter '{}' applied for API Key: {}", rateLimiter.getName(), apiKey);
    }
    return allowed;
  }

  private RateLimiter getRateLimiterForApiKey(String apiKey) {
    return rateLimiterMap.entrySet().stream()
        .filter(entry -> apiKey.startsWith(entry.getKey()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(defaultRateLimiter);
  }

  /**
   * Mengembalikan estimasi waktu hingga izin berikutnya tersedia dalam milidetik. Jika izin
   * langsung tersedia, hasilnya adalah 0.
   */
  public long getRetryAfterMillis(String apiKey) {
    RateLimiter rateLimiter = getRateLimiterForApiKey(apiKey);
    return rateLimiter.reservePermission() / 1_000_000;
  }
}
