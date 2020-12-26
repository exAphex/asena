package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.model.Log.LogType;
import com.asena.scimgateway.model.dto.LogDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogDTOTest {

    @Test
    void toDTOTest() {
        Log l = new Log();
        Instant n = Instant.now();
        l.setId(1);
        l.setTimestamp(n);
        l.setType(LogType.INFO);
        l.setMessage("OMG");
        
        LogDTO lDTO = LogDTO.toDTO(l);

        assertEquals(1, lDTO.getId());
        assertEquals(n, lDTO.getTimestamp());
        assertEquals(LogType.INFO, lDTO.getType());
        assertEquals("OMG", lDTO.getMessage());
        
        String s = "";
        for (int i = 0; i < 512; i++) {
            s += "TESTSTR";
        }
        l.setMessage(s);
        lDTO = LogDTO.toDTO(l);
        assertEquals(1024, lDTO.getMessage().length());

        l.setMessage(null);
        lDTO = LogDTO.toDTO(l);
        assertNull(lDTO.getMessage());
        
        l = null;
        lDTO = LogDTO.toDTO(l);
        assertNull(lDTO);
    }

    @Test
    void toDTOListTest() {
        Log l = new Log();
        Instant n = Instant.now();
        l.setId(1);
        l.setTimestamp(n);
        l.setType(LogType.INFO);
        l.setMessage("OMG");

        Log lTwo = new Log();
        Instant nTwo = Instant.now();
        l.setId(1);
        l.setTimestamp(nTwo);
        l.setType(LogType.ERROR);
        l.setMessage("OMG2");

        List<Log> ls = new ArrayList<>();
        ls.add(l);
        ls.add(lTwo);
        ls.add(null);

        List<LogDTO> lsDTO = LogDTO.toDTO(ls);
        assertEquals(3, lsDTO.size());

        ls = null;
        lsDTO = LogDTO.toDTO(ls);
        assertEquals(0, lsDTO.size());
    }
}