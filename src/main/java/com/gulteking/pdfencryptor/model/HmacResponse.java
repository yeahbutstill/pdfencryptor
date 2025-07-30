package com.gulteking.pdfencryptor.model;

import lombok.Value;

@Value
public class HmacResponse {
    String status;
    String hmac;
    String timestamp;
    String errorMessage;
}
