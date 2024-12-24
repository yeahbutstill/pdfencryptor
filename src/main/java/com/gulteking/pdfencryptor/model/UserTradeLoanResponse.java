package com.gulteking.pdfencryptor.model;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTradeLoanResponse {
  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a valid PDF file")
  @Column(nullable = false)
  private String file;

  @NotBlank
  @Pattern(regexp = "\\d{6}", message = "CIF must be exactly 6 numeric digits")
  @Column(nullable = false)
  private String cif;
}
