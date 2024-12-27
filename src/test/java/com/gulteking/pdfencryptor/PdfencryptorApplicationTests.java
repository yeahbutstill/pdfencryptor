package com.gulteking.pdfencryptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gulteking.pdfencryptor.config.ApiKeyFilterConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PdfencryptorApplicationTests {

  @Test
  void testShouldFilter() {
    ApiKeyFilterConfiguration config = new ApiKeyFilterConfiguration();
    ReflectionTestUtils.setField(config, "protectedPaths", new String[] {"/api/v1/users/*"});
    assertTrue(config.shouldFilter("/api/v1/users/tradeconfo"));
    assertTrue(config.shouldFilter("/api/v1/users/tradeloan"));
    assertTrue(config.shouldFilter("/api/v1/users/tax"));
    assertFalse(config.shouldFilter("/api/v1/admin"));
  }
}
