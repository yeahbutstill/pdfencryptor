package com.gulteking.pdfencryptor.model;

import lombok.Value;

@Value
public class HmacRequest {
    String timestamp;
    String requestBody;
}
