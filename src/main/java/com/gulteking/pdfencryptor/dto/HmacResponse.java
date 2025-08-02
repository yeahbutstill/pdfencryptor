package com.gulteking.pdfencryptor.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

@Value
public class HmacResponse {
    String status;
    String hmac;
    String timestamp;
    String errorMessage;
    JsonNode requestBody; // Changed to JsonNode to return JSON object
}
