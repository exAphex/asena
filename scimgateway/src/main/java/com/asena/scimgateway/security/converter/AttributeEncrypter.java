package com.asena.scimgateway.security.converter;

import java.security.AlgorithmParameters;
import java.security.Key;

import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AttributeEncrypter implements AttributeConverter<String, String> {

    private Logger logger = LoggerFactory.getLogger(AttributeEncrypter.class);
    private static final String AES = "AES/CBC/PKCS5Padding";
    private final Key key;
    private final Cipher cipher;
    

    public AttributeEncrypter(@Value("${com.asena.scimgateway.security.key}") String SECRET) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        key = new SecretKeySpec(keyBytes, "AES"); 
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] enc = cipher.doFinal(attribute.getBytes());
            byte[] concat = new byte[iv.length + enc.length];
            System.arraycopy(iv, 0, concat, 0, iv.length);
            System.arraycopy(enc, 0, concat, iv.length, enc.length);
            return Base64.getEncoder().encodeToString(concat);
        } catch (Exception e) {
            logger.error("Encryption error", e);
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            byte[] enc = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[16];
            byte[] dec = new byte[enc.length - 16];
            System.arraycopy(enc, 0, iv, 0, 16);
            System.arraycopy(enc, 16, dec, 0, enc.length - 16); 
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(dec));
        } catch (Exception e) {
            logger.error("Encryption error", e);
            return null;
        }
    }
}