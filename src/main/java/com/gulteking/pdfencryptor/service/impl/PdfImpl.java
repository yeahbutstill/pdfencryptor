package com.gulteking.pdfencryptor.service.impl;

import com.gulteking.pdfencryptor.entity.TaxUsers;
import com.gulteking.pdfencryptor.entity.TradeConfoUsers;
import com.gulteking.pdfencryptor.entity.TradeLoanUsers;
import com.gulteking.pdfencryptor.model.UserTaxResponse;
import com.gulteking.pdfencryptor.model.UserTradeConfoResponse;
import com.gulteking.pdfencryptor.model.UserTradeLoanResponse;
import com.gulteking.pdfencryptor.repository.TaxUserRepository;
import com.gulteking.pdfencryptor.repository.TradeConfoUserRepository;
import com.gulteking.pdfencryptor.repository.TradeLoanUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class PdfImpl {
  private static final String FNE = "File must not be null or empty.";
  private static final String FNAMENE = "File name cannot be null or empty.";

  private TradeConfoUserRepository tradeConfoUserRepository;
  private TradeLoanUserRepository tradeLoanUserRepository;
  private TaxUserRepository taxUserRepository;
  private ValidationService validationService;
  private PdfToFolderImpl pdfEncryptorServiceImpl;

  public PdfImpl(
      TradeConfoUserRepository tradeConfoUserRepository,
      TradeLoanUserRepository tradeLoanUserRepository,
      TaxUserRepository taxUserRepository,
      ValidationService validationService,
      PdfToFolderImpl pdfEncryptorServiceImpl) {
    this.tradeConfoUserRepository = tradeConfoUserRepository;
    this.tradeLoanUserRepository = tradeLoanUserRepository;
    this.taxUserRepository = taxUserRepository;
    this.validationService = validationService;
    this.pdfEncryptorServiceImpl = pdfEncryptorServiceImpl;
  }

  @Transactional
  public UserTradeConfoResponse generatePasswordPdfConfo(
      MultipartFile file, String depositoNumber) {
    // Validasi input
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException(FNE);
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException(FNAMENE);
    }

    if (depositoNumber == null || !depositoNumber.matches("\\d{12}")) {
      throw new IllegalArgumentException("Deposito Number must be a 12-digit value.");
    }

    log.info(
        "REQUEST CONFO: fileName={}, depositoNumber=******",
        fileName); // Hindari logging data sensitif

    // Validasi PDF
    validationService.validate(file);
    pdfEncryptorServiceImpl.validatePdf(file);

    // Enkripsi PDF
    String encryptedFilePath = pdfEncryptorServiceImpl.encryptPdfConfo(file, depositoNumber);

    // Buat entitas pengguna
    TradeConfoUsers tradeConfoUsers = new TradeConfoUsers();
    tradeConfoUsers.setFile(fileName);
    tradeConfoUsers.setDepositoNumber(depositoNumber);
    tradeConfoUsers.setFilePath(encryptedFilePath);

    // Simpan ke database
    tradeConfoUserRepository.save(tradeConfoUsers);

    // Kembalikan respons
    return UserTradeConfoResponse.builder()
        .file(tradeConfoUsers.getFile())
        .depositoNumber(tradeConfoUsers.getDepositoNumber())
        .build();
  }

  @Transactional
  public UserTradeLoanResponse generatePasswordPdfLoan(MultipartFile file, String cif) {
    // Validasi input
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException(FNE);
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException(FNE);
    }

    if (cif == null || !cif.matches("\\d{6}")) {
      throw new IllegalArgumentException("CIF must be a 6-digit value.");
    }

    log.info("REQUEST LOAN: fileName={}, cif=******", fileName); // Hindari logging data sensitif

    // Validasi PDF
    validationService.validate(file);
    pdfEncryptorServiceImpl.validatePdf(file);

    // Enkripsi PDF
    String encryptedFilePath = pdfEncryptorServiceImpl.encryptPdfLoan(file, cif);

    // Buat entitas pengguna
    TradeLoanUsers tradeLoanUsers = new TradeLoanUsers();
    tradeLoanUsers.setFile(fileName);
    tradeLoanUsers.setCif(cif);
    tradeLoanUsers.setFilePath(encryptedFilePath);

    // Simpan ke database
    tradeLoanUserRepository.save(tradeLoanUsers);

    // Kembalikan respons
    return UserTradeLoanResponse.builder()
        .file(tradeLoanUsers.getFile())
        .cif(tradeLoanUsers.getCif())
        .build();
  }

  @Transactional
  public UserTaxResponse generatePasswordPdfTax(MultipartFile file, String npwp) {
    // Validasi input
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException(FNE);
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException(FNE);
    }

    if (npwp == null || !npwp.matches("\\d{15}")) {
      throw new IllegalArgumentException("NPWP must be a 15-digit value.");
    }

    log.info("REQUEST TAX: fileName={}, npwp=******", fileName); // Hindari logging data sensitif

    // Validasi PDF
    validationService.validate(file);
    pdfEncryptorServiceImpl.validatePdf(file);

    // Enkripsi PDF
    String encryptedFilePath = pdfEncryptorServiceImpl.encryptPdfTax(file, npwp);

    // Buat entitas pengguna
    TaxUsers taxUsers = new TaxUsers();
    taxUsers.setFile(fileName);
    taxUsers.setNpwp(npwp);
    taxUsers.setFilePath(encryptedFilePath);

    // Simpan ke database
    taxUserRepository.save(taxUsers);

    // Kembalikan respons
    return UserTaxResponse.builder().file(taxUsers.getFile()).npwp(taxUsers.getNpwp()).build();
  }
}
