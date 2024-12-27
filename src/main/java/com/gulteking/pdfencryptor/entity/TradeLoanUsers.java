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
@Table(name = "trade_loan_users")
public class TradeLoanUsers extends AuditTable<Long> {

  @NotBlank
  @Pattern(regexp = "\\d{6}", message = "CIF must be exactly 6 numeric digits")
  @Column(nullable = false)
  private String cif;

  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a valid PDF file")
  @Column(nullable = false)
  private String file;

  @Column(nullable = false)
  private String filePath;
}
