package com.gulteking.pdfencryptor.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface HmacService {
    String generateHmac(String secretKey, String isoTimestamp, String requestBody)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalArgumentException;

    Map<String, String> generateSignature(Map<String, Object> request) throws NoSuchAlgorithmException, InvalidKeyException;
}
