package com.gulteking.pdfencryptor.config;

import com.gulteking.pdfencryptor.helpers.JsonResponseUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  private final ApiKeyValidator apiKeyValidator;
  private final ResilienceRateLimiter rateLimiter;
  private final ApiKeyFilterConfiguration apiKeyFilterConfiguration;

  public ApiKeyFilter(
      ApiKeyValidator apiKeyValidator,
      ResilienceRateLimiter rateLimiter,
      ApiKeyFilterConfiguration apiKeyFilterConfiguration) {
    this.apiKeyValidator = apiKeyValidator;
    this.rateLimiter = rateLimiter;
    this.apiKeyFilterConfiguration = apiKeyFilterConfiguration;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    log.info("Entering ApiKeyFilter for URI: {}", request.getRequestURI());
    String apiKey = request.getHeader("X-API-KEY");
    String requestUri = request.getRequestURI();
    String httpMethod = request.getMethod();
    String remoteAddr = request.getRemoteAddr();

    // Debug jika requestUri di-filter
    if (!apiKeyFilterConfiguration.shouldFilter(requestUri)) {
      log.info("Skipping API Key filter for URI: {}", requestUri);
      filterChain.doFilter(request, response);
      return;
    }

    // Validasi API Key
    if (apiKey == null || !apiKeyValidator.isValid(apiKey)) {
      log.warn(
          "Unauthorized access from IP {} to {} {}. Missing/Invalid API Key: {}",
          remoteAddr,
          httpMethod,
          requestUri,
          apiKey);

      sendErrorResponse(
          response,
          HttpStatus.UNAUTHORIZED,
          "UNAUTHORIZED",
          "Invalid or Missing API Key",
          "Required API Key");
      return;
    }

    // Validasi Rate Limiting
    if (!rateLimiter.isAllowed(apiKey)) {
      log.warn("Rate limit exceeded for API Key: {} at {} {}", apiKey, httpMethod, requestUri);

      // Opsional: tambahkan Retry-After header jika rate limiter mendukung estimasi waktu retry
      long retryAfterMillis = rateLimiter.getRetryAfterMillis(apiKey);
      if (retryAfterMillis > 0) {
        response.setHeader("Retry-After", String.valueOf(retryAfterMillis / 1000));
      }

      sendErrorResponse(
          response,
          HttpStatus.TOO_MANY_REQUESTS,
          "TOO_MANY_REQUESTS",
          "Rate limit exceeded. Please try again later.",
          "Rate limiter triggered");
      return;
    }

    // Lanjutkan ke filter berikutnya
    filterChain.doFilter(request, response);
  }

  private void sendErrorResponse(
      HttpServletResponse response,
      HttpStatus status,
      String errorCode,
      String errorMessage,
      String devMessage)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response
        .getWriter()
        .write(
            JsonResponseUtil.createErrorResponse(
                LocalDateTime.now(), status.value(), errorCode, errorMessage, devMessage));
  }
}
