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
public class UserTaxResponse {
  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a PDF")
  @Column(nullable = false)
  private String file;

  @NotBlank
  @Pattern(regexp = "\\d{15}", message = "NPWP must be exactly 15 digits")
  @Column(nullable = false)
  private String npwp;
}
