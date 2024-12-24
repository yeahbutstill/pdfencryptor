package com.gulteking.pdfencryptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.gulteking.pdfencryptor.config.ApiKeyValidator;
import com.gulteking.pdfencryptor.repository.ApiKeyRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Disabled
class ApiKeyValidatorTest {

  @MockBean private ApiKeyRepository apiKeyRepository;

  @Autowired private ApiKeyValidator apiKeyValidator;

  @Test
  void testApiKeyCaching() {
    String apiKey = "TestApiKey";

    // Simulasi hasil query repository
    when(apiKeyRepository.existsByApiKey(apiKey)).thenReturn(true);

    // Panggilan pertama, harus ke repository
    assertTrue(apiKeyValidator.isValid(apiKey));
    verify(apiKeyRepository, times(1)).existsByApiKey(apiKey);

    // Panggilan kedua, hasil diambil dari cache
    assertTrue(apiKeyValidator.isValid(apiKey));
    verify(apiKeyRepository, times(1)).existsByApiKey(apiKey);
  }
}
