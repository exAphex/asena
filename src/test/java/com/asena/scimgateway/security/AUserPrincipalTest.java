package com.asena.scimgateway.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.asena.scimgateway.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AUserPrincipalTest {
    
    @Test
    void testAUserPrincipal() {
        User u = new User();
        u.setPassword("password");
        u.setUserName("testuser");

        AUserPrincipal ap = new AUserPrincipal(u);
        assertNull(ap.getAuthorities());
        assertEquals("password", ap.getPassword());
        assertEquals("testuser", ap.getUsername());
        assertTrue(ap.isAccountNonExpired());
        assertTrue(ap.isAccountNonLocked());
        assertTrue(ap.isCredentialsNonExpired());
        assertTrue(ap.isEnabled()); 
    }
}