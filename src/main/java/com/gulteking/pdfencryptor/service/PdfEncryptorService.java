package com.gulteking.pdfencryptor.service;

import org.springframework.web.multipart.MultipartFile;

public interface PdfEncryptorService {
  byte[] encryptPdf(MultipartFile file, String password);

  void validatePdf(MultipartFile file);
}
