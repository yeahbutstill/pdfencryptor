package com.gulteking.pdfencryptor.repository;

import com.gulteking.pdfencryptor.entity.ApiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeys, Long> {
  boolean existsByApiKey(String apiKey);
}
