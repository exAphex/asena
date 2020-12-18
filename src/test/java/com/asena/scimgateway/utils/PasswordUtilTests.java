package com.asena.scimgateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordUtilTests {
    @Test
    void generatePasswordTest() {
        String s = PasswordUtil.generatePassword(3);
        assertEquals(3, s.length());

        s = PasswordUtil.generatePassword(0);
        assertEquals(0, s.length()); 

        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.generatePassword(-1);
        });
    }
}