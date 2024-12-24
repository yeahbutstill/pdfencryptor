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
public class UserTradeConfoResponse {
  @NotBlank
  @Pattern(regexp = ".*\\.pdf$", message = "File must be a PDF")
  @Column(nullable = false)
  private String file;

  @NotBlank
  @Pattern(regexp = "\\d{12}", message = "Deposit Number must be exactly 12 digits")
  @Column(nullable = false)
  private String depositoNumber;
}
