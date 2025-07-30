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
import java.util.Optional;

@Setter
@Slf4j
@Service
public class PdfEncryptorServiceImpl implements PdfEncryptorService {

    @Value("${pdf.key.length}")
    private int keyLength;

    @Override
    public byte[] encryptPdf(MultipartFile file, String password) {
        validatePdf(file);
        validatePassword(password);

        try (PDDocument doc = Loader.loadPDF(file.getBytes());
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            StandardProtectionPolicy spp = getStandardProtectionPolicy(password);
            doc.protect(spp);
            doc.save(baos);

            return baos.toByteArray();
        } catch (Exception ex) {
            log.error("Terjadi kesalahan saat mengenkripsi PDF", ex);
            throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
        }
    }

    private StandardProtectionPolicy getStandardProtectionPolicy(String password) {
        if (keyLength != 128 && keyLength != 256) {
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

        StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
        spp.setEncryptionKeyLength(keyLength);
        return spp;
    }

    @Override
    public void validatePdf(MultipartFile file) {
        String filename =
                Optional.ofNullable(file)
                        .map(MultipartFile::getOriginalFilename)
                        .orElseThrow(
                                () -> new IllegalArgumentException("File atau nama file tidak boleh null"));

        if (filename.isEmpty()) {
            throw new GlobalException(ExceptionMessages.INVALID_FILE_NAME);
        }

        if (!filename.toLowerCase().endsWith(".pdf")) {
            throw new GlobalException(ExceptionMessages.INVALID_FILE_EXTENSION);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong atau null");
        }
    }
}
