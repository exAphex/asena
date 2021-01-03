package com.asena.scimgateway.utils;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtil {
    private PasswordUtil() {}

    public static String generatePassword(int length) {
        char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")).toCharArray();
        String randomStr = RandomStringUtils.random(length, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
        return randomStr;
    }
}