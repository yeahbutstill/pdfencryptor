package com.gulteking.pdfencryptor.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FilterConfig {

  @Value("${filter.api.key.patterns:/api/v1/users/*}")
  private String[] urlPatterns; // Tidak perlu injeksi konstruktor di sini

  @Bean
  public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(
      ApiKeyValidator apiKeyValidator,
      ResilienceRateLimiter rateLimiter,
      ApiKeyFilterConfiguration apiKeyFilterConfiguration) {

    FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
    ApiKeyFilter apiKeyFilter =
        new ApiKeyFilter(apiKeyValidator, rateLimiter, apiKeyFilterConfiguration);

    // Setel Filter
    registrationBean.setFilter(apiKeyFilter);

    // Pola URL
    registrationBean.addUrlPatterns(urlPatterns);

    // Prioritas Filter
    registrationBean.setOrder(1); // Tentukan urutan filter dalam filter chain

    log.info("ApiKeyFilter terdaftar untuk pola: {}", Arrays.toString(urlPatterns)); // Log pola URL
    return registrationBean;
  }
}
