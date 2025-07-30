package com.gulteking.pdfencryptor.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionModel {

    private String error;
    private String errorDescription;
    private String internalError;
    private String internalErrorDescription;
}
