package com.asena.scimgateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<PasswordUtil> constructor = PasswordUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}