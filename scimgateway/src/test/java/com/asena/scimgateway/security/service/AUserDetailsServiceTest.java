package com.asena.scimgateway.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asena.scimgateway.model.User;
import com.asena.scimgateway.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AUserDetailsServiceTest {
    @Autowired
    private AUserDetailsService aUserDetailsService;

    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsernameTest() {
        User u = userService.createServiceUser("TEST");

        UserDetails ud = aUserDetailsService.loadUserByUsername(u.getUserName());
        assertEquals(u.getUserName(), ud.getUsername());
        
        assertThrows(UsernameNotFoundException.class, () -> {
            aUserDetailsService.loadUserByUsername(null);
        });

        assertThrows(UsernameNotFoundException.class, () -> {
            aUserDetailsService.loadUserByUsername("");
        });
    }
}