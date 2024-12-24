package com.gulteking.pdfencryptor.service;

import org.springframework.web.multipart.MultipartFile;

public interface PdfEncryptorService {
  String encryptPdfConfo(MultipartFile file, String password);

  String encryptPdfLoan(MultipartFile file, String password);

  String encryptPdfTax(MultipartFile file, String password);

  void validatePdf(MultipartFile file);
}
