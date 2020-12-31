package com.asena.scimgateway.security.converter;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AttributeEncrypter implements AttributeConverter<String, String> {

    private static final String AES = "AES/CBC/PKCS5Padding";
    byte[] salt = new String("ASENA").getBytes();
    int iterationCount = 1024;
    int keyStrength = 256;

    private final Key key;
    private final Cipher cipher;
    byte[] iv;

    public AttributeEncrypter(@Value("${com.asena.scimgateway.security.key}") String SECRET) throws Exception {
        KeySpec spec = new PBEKeySpec(SECRET.toCharArray(), salt, iterationCount, keyStrength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey tmp = factory.generateSecret(spec);
        key = new SecretKeySpec(tmp.getEncoded(), "AES");
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            return Base64.getEncoder().encodeToString(iv) + "|" + Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | InvalidParameterSpecException | BadPaddingException
                | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            String[] arrParams = dbData.split("\\|");
            String strIV = arrParams[0];
            String strPass = arrParams[1];
            iv = Base64.getDecoder().decode(strIV);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(Base64.getDecoder().decode(strPass)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }
}