package com.gulteking.pdfencryptor.service.impl;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.GlobalException;
import com.gulteking.pdfencryptor.exception.InternalException;
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
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Setter
@Slf4j
@Service
public class PdfEncryptorServiceImpl implements PdfEncryptorService {

    @Value("${pdf.key.length}")
    private int keyLength;

    @Override
    public byte[] encryptPdf(MultipartFile file, String password) {
        log.info("Starting PDF encryption for file: {}",
                Optional.ofNullable(file).map(MultipartFile::getOriginalFilename).orElse("null"));

        validatePdf(file);
        validatePassword(password);
        log.info("File and password validation passed");

        try (PDDocument doc = Loader.loadPDF(Objects.requireNonNull(file).getBytes());
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            log.info("PDF loaded successfully, number of pages: {}", doc.getNumberOfPages());

            if (doc.getNumberOfPages() == 0) {
                log.warn("PDF file is empty");
                throw new GlobalException(ExceptionMessages.EMPTY_PDF_FILE);
            }

            StandardProtectionPolicy spp = getStandardProtectionPolicy(password);
            log.info("Applying protection policy with key length: {}", keyLength);
            doc.protect(spp);
            doc.save(baos);
            log.info("PDF encrypted and saved to output stream");

            return baos.toByteArray();
        } catch (IOException ex) {
            log.error("Failed to process PDF file", ex);
            throw new InternalException(ExceptionMessages.PDF_PROCESSING_ERROR, ex);
        } catch (Exception ex) {
            log.error("Unexpected error during PDF encryption", ex);
            throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
        }
    }

    private StandardProtectionPolicy getStandardProtectionPolicy(String password) {
        log.info("Configuring protection policy with key length: {}", keyLength);
        if (keyLength != 128 && keyLength != 256) {
            log.error("Invalid key length: {}", keyLength);
            throw new IllegalArgumentException("Panjang kunci harus 128 atau 256 bit");
        }

        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        ap.setCanAssembleDocument(false);
        ap.setCanExtractContent(false);
        ap.setCanExtractForAccessibility(false);
        ap.setCanFillInForm(false);
        ap.setCanModify(false);
        ap.setCanModifyAnnotations(false);
        log.info("Access permissions configured: print={}, assemble={}, extract={}, modify={}",
                ap.canPrint(), ap.canAssembleDocument(), ap.canExtractContent(), ap.canModify());

        StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
        spp.setEncryptionKeyLength(keyLength);
        return spp;
    }

    @Override
    public void validatePdf(MultipartFile file) {
        log.info("Validating PDF file");
        String filename = Optional.ofNullable(file)
                .map(MultipartFile::getOriginalFilename)
                .orElseThrow(() -> {
                    log.error("File or filename is null");
                    return new IllegalArgumentException("File atau nama file tidak boleh null");
                });

        if (filename.isEmpty()) {
            log.error("Filename is empty");
            throw new GlobalException(ExceptionMessages.INVALID_FILE_NAME);
        }

        if (!filename.toLowerCase().endsWith(".pdf")) {
            log.error("Invalid file extension for file: {}", filename);
            throw new GlobalException(ExceptionMessages.INVALID_FILE_EXTENSION);
        }
        log.info("PDF file validation passed for file: {}", filename);
    }

    private void validatePassword(String password) {
        log.info("Validating password");
        if (password == null || password.isEmpty()) {
            log.error("Password is null or empty");
            throw new IllegalArgumentException("Password tidak boleh kosong atau null");
        }
        log.info("Password validation passed");
    }
}