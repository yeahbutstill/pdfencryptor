package com.gulteking.pdfencryptor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "api_keys")
public class ApiKeys extends AuditTable<Long> {

  @Column(unique = true, nullable = false)
  private String apiKey;

  @Column(nullable = false)
  private String description; // Tambahan deskripsi API Key (opsional)
}
