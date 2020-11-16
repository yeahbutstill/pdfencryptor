package com.gulteking.pdfencryptor.controller;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.PdfException;
import com.gulteking.pdfencryptor.service.PdfEncryptorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfEncryptionController {
    private final PdfEncryptorService pdfEncryptorService;

    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] encryptPdf(@RequestParam("file") MultipartFile file,
                             @RequestParam("password") String password) {
        validatePdf(file);
        return pdfEncryptorService.encryptPdf(file, password);
    }

    private void validatePdf(MultipartFile file) {
        if(file.getOriginalFilename()==null){
            throw new PdfException(ExceptionMessages.INVALID_FILE_NAME);
        }
        if (!file.getOriginalFilename().endsWith(".pdf") && !file.getOriginalFilename().endsWith(".PDF")) {
            throw new PdfException(ExceptionMessages.INVALID_FILE_EXTENSION);
        }
        if (file.isEmpty()) {
            throw new PdfException(ExceptionMessages.EMPTY_PDF_FILE);
        }
    }
}
