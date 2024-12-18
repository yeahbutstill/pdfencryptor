package com.gulteking.pdfencryptor.controller;

import com.gulteking.pdfencryptor.service.impl.PdfToFolderImpl;
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

    private final PdfToFolderImpl pdfEncryptorServiceImpl;

    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] encryptPdf(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) {

        pdfEncryptorServiceImpl.validatePdf(file);
        return pdfEncryptorServiceImpl.encryptPdf(file, password);

    }

}