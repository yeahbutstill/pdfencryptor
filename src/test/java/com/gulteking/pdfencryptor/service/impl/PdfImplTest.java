package com.gulteking.pdfencryptor.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gulteking.pdfencryptor.entity.TaxUsers;
import com.gulteking.pdfencryptor.entity.TradeConfoUsers;
import com.gulteking.pdfencryptor.entity.TradeLoanUsers;
import com.gulteking.pdfencryptor.model.UserTaxResponse;
import com.gulteking.pdfencryptor.model.UserTradeConfoResponse;
import com.gulteking.pdfencryptor.model.UserTradeLoanResponse;
import com.gulteking.pdfencryptor.repository.TaxUserRepository;
import com.gulteking.pdfencryptor.repository.TradeConfoUserRepository;
import com.gulteking.pdfencryptor.repository.TradeLoanUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class PdfImplTest {

  @InjectMocks private PdfImpl pdfImpl;

  @Mock private TradeConfoUserRepository tradeConfoUserRepository;

  @Mock private TradeLoanUserRepository tradeLoanUserRepository;

  @Mock private TaxUserRepository taxUserRepository;

  @Mock private ValidationService validationService;

  @Mock private PdfToFolderImpl pdfEncryptorServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void generatePasswordPdfConfo_Success() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String depositoNumber = "123456789012";
    String encryptedFilePath = "/path/to/encrypted/test.pdf";

    when(pdfEncryptorServiceImpl.encryptPdfConfo(file, depositoNumber))
        .thenReturn(encryptedFilePath);

    UserTradeConfoResponse response = pdfImpl.generatePasswordPdfConfo(file, depositoNumber);

    assertNotNull(response);
    assertEquals("test.pdf", response.getFile());
    assertEquals(depositoNumber, response.getDepositoNumber());

    verify(validationService).validate(file);
    verify(pdfEncryptorServiceImpl).validatePdf(file);
    verify(tradeConfoUserRepository).save(any(TradeConfoUsers.class));
  }

  @Test
  void generatePasswordPdfConfo_InvalidDepositoNumber() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String invalidDepositoNumber = "12345";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              pdfImpl.generatePasswordPdfConfo(file, invalidDepositoNumber);
            });

    assertEquals("Deposito Number must be a 12-digit value.", exception.getMessage());
  }

  @Test
  void generatePasswordPdfLoan_Success() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String cif = "123456";
    String encryptedFilePath = "/path/to/encrypted/test.pdf";

    when(pdfEncryptorServiceImpl.encryptPdfLoan(file, cif)).thenReturn(encryptedFilePath);

    UserTradeLoanResponse response = pdfImpl.generatePasswordPdfLoan(file, cif);

    assertNotNull(response);
    assertEquals("test.pdf", response.getFile());
    assertEquals(cif, response.getCif());

    verify(validationService).validate(file);
    verify(pdfEncryptorServiceImpl).validatePdf(file);
    verify(tradeLoanUserRepository).save(any(TradeLoanUsers.class));
  }

  @Test
  void generatePasswordPdfLoan_InvalidCif() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String invalidCif = "12345";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              pdfImpl.generatePasswordPdfLoan(file, invalidCif);
            });

    assertEquals("CIF must be a 6-digit value.", exception.getMessage());
  }

  @Test
  void generatePasswordPdfTax_Success() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String npwp = "123456789012345";
    String encryptedFilePath = "/path/to/encrypted/test.pdf";

    when(pdfEncryptorServiceImpl.encryptPdfTax(file, npwp)).thenReturn(encryptedFilePath);

    UserTaxResponse response = pdfImpl.generatePasswordPdfTax(file, npwp);

    assertNotNull(response);
    assertEquals("test.pdf", response.getFile());
    assertEquals(npwp, response.getNpwp());

    verify(validationService).validate(file);
    verify(pdfEncryptorServiceImpl).validatePdf(file);
    verify(taxUserRepository).save(any(TaxUsers.class));
  }

  @Test
  void generatePasswordPdfTax_InvalidNpwp() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
    String invalidNpwp = "12345";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              pdfImpl.generatePasswordPdfTax(file, invalidNpwp);
            });

    assertEquals("NPWP must be a 15-digit value.", exception.getMessage());
  }

  @Test
  void generatePasswordPdfConfo_EmptyFile() {
    MockMultipartFile emptyFile =
        new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[0]);
    String depositoNumber = "123456789012";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              pdfImpl.generatePasswordPdfConfo(emptyFile, depositoNumber);
            });

    assertEquals("File must not be null or empty.", exception.getMessage());
  }

  @Test
  void generatePasswordPdfConfo_NullFileName() {
    MockMultipartFile file =
        new MockMultipartFile("file", null, "application/pdf", "dummy content".getBytes());
    String depositoNumber = "123456789012";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              pdfImpl.generatePasswordPdfConfo(file, depositoNumber);
            });

    assertEquals("File name cannot be null or empty.", exception.getMessage());
  }
}
