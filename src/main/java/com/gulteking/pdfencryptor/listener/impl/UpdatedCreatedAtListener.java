package com.gulteking.pdfencryptor.listener.impl;

import com.gulteking.pdfencryptor.listener.UpdatedCreatedAtAware;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;

public class UpdatedCreatedAtListener {

  @PrePersist
  public void setLastCreatedAt(UpdatedCreatedAtAware object) {
    object.setCreatedAt(LocalDateTime.now());
  }
}
