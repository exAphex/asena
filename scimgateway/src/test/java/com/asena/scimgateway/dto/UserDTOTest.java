package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.User.UserType;
import com.asena.scimgateway.model.dto.UserDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDTOTest {

    @Test
    void fromDTOTest() {
        UserDTO uDTO = new UserDTO();
        uDTO.setId(0);
        uDTO.setType(UserType.ADMIN);
        uDTO.setMail("testmail");
        uDTO.setPassword("testpass");
        uDTO.setUserName("testname");

        User u = uDTO.fromDTO();
        assertEquals(0, u.getId());
        assertEquals(UserType.ADMIN, u.getType());
        assertEquals("testmail", u.getMail());
        assertEquals("testpass", u.getPassword());
        assertEquals("testname", u.getUserName());
    }

    @Test
    void toDTOTest() {
        User u = new User();
        u.setId(0);
        u.setType(UserType.ADMIN);
        u.setMail("testmail");
        u.setPassword("testpass");
        u.setUserName("testname");

        UserDTO uDTO = UserDTO.toDTO(u);
        assertEquals(0, uDTO.getId());
        assertEquals(UserType.ADMIN, uDTO.getType());
        assertEquals("testmail", uDTO.getMail());
        assertEquals("testpass", uDTO.getPassword());
        assertEquals("testname", uDTO.getUserName());

        uDTO = UserDTO.toDTO(null);
        assertNull(uDTO);
    }
}