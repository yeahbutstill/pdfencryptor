package com.gulteking.pdfencryptor.entity;

import com.gulteking.pdfencryptor.listener.UpdatedCreatedAtAware;
import com.gulteking.pdfencryptor.listener.impl.UpdatedCreatedAtListener;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// Ini hanya sebuah parent class tapi bukan IS-A Relationship
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class, UpdatedCreatedAtListener.class})
// ini dimana T itu harus extends Serializable
public abstract class AuditTable<T extends Serializable> implements UpdatedCreatedAtAware {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private T id;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
