package com.gulteking.pdfencryptor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "tax_users")
public class TaxUsers extends AuditTableEntity<Long> {

  @NotBlank
  @Pattern(regexp = "\\d{15}", message = "NPWP must be exactly 15 digits")
  @Column(nullable = false)
  private String npwp;

  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a PDF")
  @Column(nullable = false)
  private String file;

  @Column(nullable = false)
  private String filePath;
}
