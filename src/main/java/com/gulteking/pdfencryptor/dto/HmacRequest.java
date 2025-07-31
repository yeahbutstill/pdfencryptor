package com.gulteking.pdfencryptor.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class HmacRequest {
    @NotBlank
    String requestBody;
}
