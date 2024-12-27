package com.gulteking.pdfencryptor.service.impl;

import com.gulteking.pdfencryptor.exception.ExceptionMessages;
import com.gulteking.pdfencryptor.exception.InternalException;
import com.gulteking.pdfencryptor.exception.PdfException;
import com.gulteking.pdfencryptor.service.PdfEncryptorService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Slf4j
@Service
public class PdfToFolderImpl implements PdfEncryptorService {

  private static final String INVALIDPATHFOLDER = "Invalid save folder path";
  private static final String CREATESAVEFOLDER = "Created save folder: {}";
  private static final String SAVEFOLDER = "Save folder is not a writable directory";
  private static final String FAILEDTOCREATE = "Failed to create or validate save folder: {}";
  private static final String EPDF = "Exception occurred while encrypting PDF";
  private static final String ENCRYPTEDS = "encrypted_%s";
  private static final String ENCRYPTEDSAVETO = "Encrypted PDF saved to: {}";

  @Value("${pdf.key.length}")
  private int keyLength;

  @Value("${pdf.save.folderconfo}")
  private String saveFolderConfo;

  @Value("${pdf.save.folderloan}")
  private String saveFolderLoan;

  @Value("${pdf.save.foldertax}")
  private String saveFolderTax;

  @Value("${pdf.max.file.size:10485760}") // Default: 10MB
  private long maxFileSize;

  @PostConstruct
  public void validateSaveFolderConfo() {
    Path savePath = Paths.get(saveFolderConfo);
    try {
      if (!Files.exists(savePath)) {
        Files.createDirectories(savePath);
        log.info(CREATESAVEFOLDER, savePath.toAbsolutePath());
      } else if (!Files.isDirectory(savePath) || !Files.isWritable(savePath)) {
        throw new IllegalStateException(SAVEFOLDER);
      }
    } catch (IOException ex) {
      log.error(FAILEDTOCREATE, savePath.toAbsolutePath(), ex);
      throw new IllegalStateException(INVALIDPATHFOLDER, ex);
    }
  }

  @PostConstruct
  public void validateSaveFolderLoan() {
    Path savePath = Paths.get(saveFolderLoan);
    try {
      if (!Files.exists(savePath)) {
        Files.createDirectories(savePath);
        log.info(CREATESAVEFOLDER, savePath.toAbsolutePath());
      } else if (!Files.isDirectory(savePath) || !Files.isWritable(savePath)) {
        throw new IllegalStateException(SAVEFOLDER);
      }
    } catch (IOException ex) {
      log.error(FAILEDTOCREATE, savePath.toAbsolutePath(), ex);
      throw new IllegalStateException(INVALIDPATHFOLDER, ex);
    }
  }

  @PostConstruct
  public void validateSaveFolderTax() {
    Path savePath = Paths.get(saveFolderTax);
    try {
      if (!Files.exists(savePath)) {
        Files.createDirectories(savePath);
        log.info(CREATESAVEFOLDER, savePath.toAbsolutePath());
      } else if (!Files.isDirectory(savePath) || !Files.isWritable(savePath)) {
        throw new IllegalStateException(SAVEFOLDER);
      }
    } catch (IOException ex) {
      log.error(FAILEDTOCREATE, savePath.toAbsolutePath(), ex);
      throw new IllegalStateException(INVALIDPATHFOLDER, ex);
    }
  }

  @Override
  public String encryptPdfConfo(MultipartFile file, String password) {
    try {
      validatePdf(file);
      validatePassword(password);

      byte[] sourcePdfBytes = file.getBytes();
      PDDocument doc = Loader.loadPDF(sourcePdfBytes);

      StandardProtectionPolicy spp = getStandardProtectionPolicy(password);
      doc.protect(spp);

      // Save to ByteArrayOutputStream
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      doc.save(baos);
      doc.close();

      // Save encrypted PDF to folder
      return saveEncryptedPdfConfo(file.getOriginalFilename(), baos.toByteArray());
    } catch (Exception ex) {
      log.error(EPDF, ex);
      throw new PdfException(ex.getMessage());
      //      throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
    }
  }

  @Override
  public String encryptPdfLoan(MultipartFile file, String password) {
    try {
      validatePdf(file);
      validatePassword(password);

      byte[] sourcePdfBytes = file.getBytes();
      PDDocument doc = Loader.loadPDF(sourcePdfBytes);

      StandardProtectionPolicy spp = getStandardProtectionPolicy(password);
      doc.protect(spp);

      // Save to ByteArrayOutputStream
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      doc.save(baos);
      doc.close();

      // Save encrypted PDF to folder
      return saveEncryptedPdfLoan(file.getOriginalFilename(), baos.toByteArray());
    } catch (Exception ex) {
      log.error(EPDF, ex);
      throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
    }
  }

  @Override
  public String encryptPdfTax(MultipartFile file, String password) {
    try {
      validatePdf(file);
      validatePassword(password);

      byte[] sourcePdfBytes = file.getBytes();
      PDDocument doc = Loader.loadPDF(sourcePdfBytes);

      StandardProtectionPolicy spp = getStandardProtectionPolicy(password);
      doc.protect(spp);

      // Save to ByteArrayOutputStream
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      doc.save(baos);
      doc.close();

      // Save encrypted PDF to folder
      return saveEncryptedPdfTax(file.getOriginalFilename(), baos.toByteArray());
    } catch (Exception ex) {
      log.error(EPDF, ex);
      throw new InternalException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
    }
  }

  public String saveEncryptedPdfConfo(String originalFilename, byte[] encryptedPdfBytes)
      throws IOException {
    //    String timestamp = String.valueOf(System.currentTimeMillis());
    //    String encryptedFileName = String.format("encrypted_%s_%s", timestamp, originalFilename);
    String encryptedFileName = String.format(ENCRYPTEDS, originalFilename);
    Path filePath = Paths.get(saveFolderConfo, encryptedFileName);
    try (BufferedOutputStream bos =
        new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
      bos.write(encryptedPdfBytes);
    }

    log.info(ENCRYPTEDSAVETO, filePath.toAbsolutePath());
    return filePath.toString(); // Return the saved file path
  }

  public String saveEncryptedPdfLoan(String originalFilename, byte[] encryptedPdfBytes)
      throws IOException {
    String encryptedFileName = String.format(ENCRYPTEDS, originalFilename);
    Path filePath = Paths.get(saveFolderLoan, encryptedFileName);
    try (BufferedOutputStream bos =
        new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
      bos.write(encryptedPdfBytes);
    }

    log.info(ENCRYPTEDSAVETO, filePath.toAbsolutePath());
    return filePath.toString(); // Return the saved file path
  }

  public String saveEncryptedPdfTax(String originalFilename, byte[] encryptedPdfBytes)
      throws IOException {
    String encryptedFileName = String.format(ENCRYPTEDS, originalFilename);
    Path filePath = Paths.get(saveFolderTax, encryptedFileName);
    try (BufferedOutputStream bos =
        new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
      bos.write(encryptedPdfBytes);
    }

    log.info(ENCRYPTEDSAVETO, filePath.toAbsolutePath());
    return filePath.toString(); // Return the saved file path
  }

  public void validatePassword(String password) {
    if (password == null || password.isEmpty()) {
      throw new PdfException("Password cannot be null or empty");
    }
    if (password.length() < 6 || password.length() > 128) {
      throw new PdfException("Password must be between 6 and 128 characters");
    }
  }

  private StandardProtectionPolicy getStandardProtectionPolicy(String password) {
    if (keyLength != 128 && keyLength != 256) {
      throw new PdfException("Key length must be 128 or 256 bits");
    }

    AccessPermission ap = new AccessPermission();
    ap.setCanPrint(false);
    ap.setCanModify(false);

    StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
    spp.setEncryptionKeyLength(keyLength);

    return spp;
  }

  @Override
  public void validatePdf(MultipartFile file) {
    String filename =
        Optional.of(file)
            .map(MultipartFile::getOriginalFilename)
            .orElseThrow(() -> new PdfException("File or filename is null"));

    if (!isValidPdf(filename)) {
      throw new PdfException("Invalid PDF file");
    }

    if (file.getSize() > maxFileSize) {
      throw new PdfException(
          String.format("File size exceeds maximum limit of %d bytes", maxFileSize));
    }
  }

  protected boolean isValidPdf(String filename) {
    return filename != null && (filename.endsWith(".pdf") || filename.endsWith(".PDF"));
  }
}
