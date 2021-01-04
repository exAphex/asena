package com.asena.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecurityUtils {
    public static String generateKey() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } 
}