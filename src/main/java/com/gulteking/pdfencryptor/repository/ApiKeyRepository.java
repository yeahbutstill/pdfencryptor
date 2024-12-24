package com.gulteking.pdfencryptor.repository;

import com.gulteking.pdfencryptor.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
  boolean existsByApiKey(String apiKey);
}
