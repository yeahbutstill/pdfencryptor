package com.gulteking.pdfencryptor.exception;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionModel {
    String errorMessage;
    String details;
}