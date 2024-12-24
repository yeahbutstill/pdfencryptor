package com.gulteking.pdfencryptor.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // Aktifkan mekanisme caching di Spring
public class CacheConfig {

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES) // Item akan kedaluwarsa 10 menit setelah ditulis
        .expireAfterAccess(
            5, TimeUnit.MINUTES) // Item akan kedaluwarsa 5 menit setelah tidak diakses
        .maximumSize(1000); // Maksimum 1000 item dalam cache
  }

  @Bean
  public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("apiKeys");
    cacheManager.setCaffeine(caffeine);
    return cacheManager;
  }
}
