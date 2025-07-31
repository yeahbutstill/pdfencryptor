package com.gulteking.pdfencryptor.dto;

import lombok.Value;

@Value
public class HmacResponse {
    String status;
    String hmac;
    String timestamp;
    String errorMessage;
    String requestBody; // To return the provided JSON as string
}
