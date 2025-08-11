package com.gulteking.pdfencryptor.controller;

import com.gulteking.pdfencryptor.service.PdfEncryptorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfEncryptionController {

    private final PdfEncryptorService pdfEncryptorService;
    //private final ApiKeyValidator apiKeyValidator;

    @PostMapping(path = "/encrypt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> encryptPdf(
            //@RequestHeader(value = "X-API-KEY") String apiKey,
            @RequestParam("file") MultipartFile file,
            @RequestParam("password") String password) {

        // Validasi API Key
        //apiKeyValidator.validateApiKey(apiKey);

        // Proses enkripsi
        byte[] encryptedPdf = pdfEncryptorService.encryptPdf(file, password);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(encryptedPdf);
    }
}
