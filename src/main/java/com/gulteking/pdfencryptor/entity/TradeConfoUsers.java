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
@Table(name = "trade_confo_users")
public class TradeConfoUsers extends AuditTableEntity<Long> {

  @NotBlank
  @Pattern(regexp = "\\d{12}", message = "Deposit Number must be exactly 12 digits")
  @Column(nullable = false)
  private String depositoNumber;

  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a PDF")
  @Column(nullable = false)
  private String file;

  @Column(nullable = false)
  private String filePath;
}
