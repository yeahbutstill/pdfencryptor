package com.gulteking.pdfencryptor.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gulteking.pdfencryptor.exception.PdfException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PdfToFolderImplTest {

  @TempDir Path tempDir;
  @InjectMocks private PdfToFolderImpl pdfToFolder;
  @Mock private MultipartFile file;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(pdfToFolder, "keyLength", 128);
    ReflectionTestUtils.setField(pdfToFolder, "maxFileSize", 10485760L); // 10 MB
    ReflectionTestUtils.setField(pdfToFolder, "saveFolderConfo", tempDir.toString());
    ReflectionTestUtils.setField(pdfToFolder, "saveFolderLoan", tempDir.toString());
    ReflectionTestUtils.setField(pdfToFolder, "saveFolderTax", tempDir.toString());
  }

  @Test
  void validateSaveFolderConfo_success() {
    assertDoesNotThrow(() -> pdfToFolder.validateSaveFolderConfo());
    Path folderPath = Paths.get(tempDir.toString());
    assertTrue(Files.isDirectory(folderPath));
    assertTrue(Files.isWritable(folderPath));
  }

  @Test
  void validateSaveFolderConfo_invalidPath() {
    ReflectionTestUtils.setField(pdfToFolder, "saveFolderConfo", "/invalid/folder/path");

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> pdfToFolder.validateSaveFolderConfo());
    assertEquals("Invalid save folder path", exception.getMessage());
  }

  @Test
  void validatePdf_success() throws IOException {
    when(file.getOriginalFilename()).thenReturn("validFile.pdf");
    when(file.getSize()).thenReturn(1024L);

    assertDoesNotThrow(() -> pdfToFolder.validatePdf(file));
  }

  @Test
  void validatePdf_invalidExtension() {
    when(file.getOriginalFilename()).thenReturn("invalidFile.txt");

    PdfException exception = assertThrows(PdfException.class, () -> pdfToFolder.validatePdf(file));
    assertEquals("Invalid PDF file", exception.getMessage());
  }

  @Test
  void validatePdf_exceedsFileSize() {
    when(file.getOriginalFilename()).thenReturn("validFile.pdf");
    when(file.getSize()).thenReturn(20485760L); // 20 MB

    PdfException exception = assertThrows(PdfException.class, () -> pdfToFolder.validatePdf(file));
    assertEquals("File size exceeds maximum limit of 10485760 bytes", exception.getMessage());
  }

  @Test
  void encryptPdfConfo_success_withRealPdf() throws IOException {
    String originalFilename = "document.pdf";
    String password = "secure123123";

    // Membaca file PDF kecil dari resource
    Path pdfPath = Paths.get("src/test/resources/test.pdf");
    byte[] pdfBytes = Files.readAllBytes(pdfPath);

    when(file.getOriginalFilename()).thenReturn(originalFilename);
    when(file.getBytes()).thenReturn(pdfBytes);

    // Jalankan metode encryptPdfConfo
    String encryptedFilePath = pdfToFolder.encryptPdfConfo(file, password);

    // Validasi hasil
    Path savedPath = Paths.get(encryptedFilePath);
    assertTrue(Files.exists(savedPath), "Encrypted file should exist");
    assertTrue(
        savedPath.toString().endsWith("encrypted_document.pdf"),
        "File name should end with encrypted_document.pdf");
  }

  @Test
  void encryptPdfConfo_invalidPassword() {
    when(file.getOriginalFilename()).thenReturn("document.pdf");

    PdfException exception =
        assertThrows(PdfException.class, () -> pdfToFolder.encryptPdfConfo(file, ""));
    assertEquals("Password cannot be null or empty", exception.getMessage());
  }

  @Test
  void encryptPdfConfo_invalidKeyLength_withRealPdf() throws IOException {
    ReflectionTestUtils.setField(pdfToFolder, "keyLength", 512); // Set key length ke nilai invalid

    // Membaca file PDF dari resource
    byte[] validPdfBytes = Files.readAllBytes(Paths.get("src/test/resources/test.pdf"));
    when(file.getOriginalFilename()).thenReturn("document.pdf");
    when(file.getBytes()).thenReturn(validPdfBytes);

    PdfException exception =
        assertThrows(PdfException.class, () -> pdfToFolder.encryptPdfConfo(file, "secure1231234"));

    assertEquals("Key length must be 128 or 256 bits", exception.getMessage());
  }

  @Test
  void saveEncryptedPdfConfo_success() throws IOException {
    String originalFilename = "document.pdf";
    byte[] encryptedBytes = {1, 2, 3};

    String encryptedFilePath = pdfToFolder.saveEncryptedPdfConfo(originalFilename, encryptedBytes);

    Path savedPath = Paths.get(encryptedFilePath);
    assertTrue(Files.exists(savedPath));
    assertTrue(Files.isReadable(savedPath));
  }

  @Test
  void saveEncryptedPdfLoan_success() throws IOException {
    String originalFilename = "loan.pdf";
    byte[] encryptedBytes = {1, 2, 3};

    String encryptedFilePath = pdfToFolder.saveEncryptedPdfLoan(originalFilename, encryptedBytes);

    Path savedPath = Paths.get(encryptedFilePath);
    assertTrue(Files.exists(savedPath));
    assertTrue(Files.isReadable(savedPath));
  }

  @Test
  void saveEncryptedPdfTax_success() throws IOException {
    String originalFilename = "tax.pdf";
    byte[] encryptedBytes = {1, 2, 3};

    String encryptedFilePath = pdfToFolder.saveEncryptedPdfTax(originalFilename, encryptedBytes);

    Path savedPath = Paths.get(encryptedFilePath);
    assertTrue(Files.exists(savedPath));
    assertTrue(Files.isReadable(savedPath));
  }

  @Test
  void isValidPdf_validExtensions() {
    assertTrue(pdfToFolder.isValidPdf("file.pdf"));
    assertTrue(pdfToFolder.isValidPdf("file.PDF"));
  }

  @Test
  void isValidPdf_invalidExtensions() {
    assertFalse(pdfToFolder.isValidPdf("file.txt"));
    assertFalse(pdfToFolder.isValidPdf(null));
  }

  @Test
  void validatePassword_validPassword() {
    assertDoesNotThrow(() -> pdfToFolder.validatePassword("secure123"));
  }

  @Test
  void validatePassword_shortPassword() {
    PdfException exception =
        assertThrows(PdfException.class, () -> pdfToFolder.validatePassword("123"));
    assertEquals("Password must be between 6 and 128 characters", exception.getMessage());
  }

  @Test
  void validatePassword_longPassword() {
    String longPassword = "a".repeat(129);

    PdfException exception =
        assertThrows(PdfException.class, () -> pdfToFolder.validatePassword(longPassword));
    assertEquals("Password must be between 6 and 128 characters", exception.getMessage());
  }
}
