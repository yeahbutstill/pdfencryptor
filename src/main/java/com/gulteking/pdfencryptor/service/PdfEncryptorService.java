package com.gulteking.pdfencryptor.service;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class PdfEncryptorService {
    @Value("${pdf.key.length}")
    private int keyLength;

    public byte[] encryptPdf(MultipartFile file, String password) {
        try {
            byte[] sourcePdfBytes = file.getBytes();
            PDDocument doc = PDDocument.load(sourcePdfBytes);

            AccessPermission ap = new AccessPermission();
            ap.setCanPrint(false);
            ap.setCanAssembleDocument(false);
            ap.setCanExtractContent(false);
            ap.setCanExtractForAccessibility(false);
            ap.setCanFillInForm(false);
            ap.setCanModify(false);
            ap.setCanModifyAnnotations(false);
            ap.setCanPrintDegraded(false);
            StandardProtectionPolicy spp = new StandardProtectionPolicy(
                    password, password, ap);

            spp.setEncryptionKeyLength(keyLength);
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

}
