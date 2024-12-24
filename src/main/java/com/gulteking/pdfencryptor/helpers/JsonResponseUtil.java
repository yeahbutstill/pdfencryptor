package com.gulteking.pdfencryptor.helpers;

import java.time.LocalDateTime;

public class JsonResponseUtil {
  public static String createErrorResponse(
      LocalDateTime timeStamp, int statusCode, String status, String message, String devMessage) {
    return """
        {
          "timeStamp": "%s",
          "statusCode": %d,
          "status": "%s",
          "message": "%s",
          "devMessage": "%s"
        }
        """
        .formatted(timeStamp, statusCode, status, message, devMessage);
  }
}
