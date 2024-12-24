package com.gulteking.pdfencryptor.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@Slf4j
public class ApiKeyFilterConfiguration {

  @Value("${api.key.protected.paths:/api/v1/users/*}")
  private String[] protectedPaths;

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @PostConstruct
  public void validateAndLogProtectedPaths() {
    if (protectedPaths == null || protectedPaths.length == 0) {
      throw new IllegalStateException("Protected paths must be configured.");
    }
    log.info("Protected paths configured: {}", Arrays.toString(protectedPaths));
  }

  public boolean shouldFilter(String requestUri) {
    log.info("Checking if URI should be filtered: {}", requestUri);

    for (String path : protectedPaths) {
      if (pathMatcher.match(path, requestUri)) {
        log.info("Matched protected path: {}", path);
        return true;
      }
    }

    log.info("No protected path matched for URI: {}", requestUri);
    return false;
  }
}
