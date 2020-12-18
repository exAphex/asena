package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteSystemService remoteSystemService;

    @BeforeEach
    void prepareDb() {
        userService.deleteAll();
        remoteSystemService.deleteAll();
    }

    @Test
    void createServiceUserTest() {
        User u = userService.createServiceUser("TEST");
        assertEquals("TEST_COMM", u.getUserName());
        assertEquals(8, u.getPassword().length());
        assertEquals(false, u.isActive());
    }

    @Test
    void findByIdTest() {
        User u = userService.createServiceUser("TEST");
        
        User usr = userService.findById(u.getId()).orElseThrow(() -> new NotFoundException(u.getId()));
        assertNotNull(usr); 

        assertThrows(NotFoundException.class, () -> {
            userService.findById(0).orElseThrow(() -> new NotFoundException(0l));
        });
    }

    @Test
    void deleteUserTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setType("LDAP");
        rs.setName("TEST");
        rs = remoteSystemService.create(rs);

        userService.deleteAll();

        User u = userService.createServiceUser("TEST1"); 
        userService.deleteById(u.getId());

        assertThrows(NotFoundException.class, () -> {
            userService.deleteById(0);
        }); 
    }


}