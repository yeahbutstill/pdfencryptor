package com.gulteking.pdfencryptor.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface HmacService {
    Map<String, String> generateSignature(Map<String, Object> request) throws NoSuchAlgorithmException, InvalidKeyException;
}
