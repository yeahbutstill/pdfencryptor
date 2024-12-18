package com.gulteking.pdfencryptor.service.impl;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.InternalException;
import com.gulteking.pdfencryptor.exception.PdfException;
import com.gulteking.pdfencryptor.service.PdfEncryptorService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Setter
@Slf4j
@Service
public class PdfEncryptorServiceImpl implements PdfEncryptorService {

    @Value("${pdf.key.length}")
    private int keyLength;

    @Override
    public byte[] encryptPdf(MultipartFile file, String password) {

        try {
            byte[] sourcePdfBytes = file.getBytes();
            PDDocument doc = Loader.loadPDF(sourcePdfBytes);

            StandardProtectionPolicy spp = getStandardProtectionPolicy(file, password);
            if (keyLength != 128 && keyLength != 256) {
                throw new IllegalArgumentException("Key length must be 128 or 256 bits");
            }

            doc.protect(spp);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            doc.close();

            return baos.toByteArray();
        } catch (Exception ex) {
            log.error("Exception occured while encrypting pdf", ex);
            throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
        }

    }

    private StandardProtectionPolicy getStandardProtectionPolicy(MultipartFile file, String password) {

        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        ap.setCanAssembleDocument(false);
        ap.setCanExtractContent(false);
        ap.setCanExtractForAccessibility(false);
        ap.setCanFillInForm(false);
        ap.setCanModify(false);
        ap.setCanModifyAnnotations(false);
        ap.setCanPrint(false);
        StandardProtectionPolicy spp = new StandardProtectionPolicy(
                password, password, ap);

        String filename = Optional.ofNullable(file)
                .map(MultipartFile::getOriginalFilename)
                .orElseThrow(() -> new IllegalArgumentException("File or filename is null"));

        if (!filename.endsWith(".pdf")) {
            throw new IllegalArgumentException("Invalid PDF file");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        spp.setEncryptionKeyLength(keyLength);
        return spp;

    }

    @Override
    public void validatePdf(MultipartFile file) {

        String filename = Optional.ofNullable(file)
                .map(MultipartFile::getOriginalFilename)
                .orElseThrow(() -> new IllegalArgumentException("File or filename is null"));

        if (filename == null || filename.isEmpty()) {
            throw new PdfException(ExceptionMessages.INVALID_FILE_NAME);
        }

        if (!filename.endsWith(".pdf") && !filename.endsWith(".PDF")) {
            throw new PdfException(ExceptionMessages.INVALID_FILE_EXTENSION);
        }

    }

}
