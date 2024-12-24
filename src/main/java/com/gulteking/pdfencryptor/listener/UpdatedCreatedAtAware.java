package com.gulteking.pdfencryptor.listener;

import java.time.LocalDateTime;

public interface UpdatedCreatedAtAware {
  void setCreatedAt(LocalDateTime localDateTime);
}
