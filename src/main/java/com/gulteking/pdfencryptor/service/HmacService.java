package com.gulteking.pdfencryptor.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface HmacService {
    String generateHmac(String secretKey, String isoTimestamp, String requestBody) throws NoSuchAlgorithmException,
            InvalidKeyException, IllegalArgumentException;
}
