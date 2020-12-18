package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.model.Log.LogType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogServiceTest {
    
    @Autowired
    private LogService logService;

    @BeforeEach
    void prepareDB() {
        logService.deleteAll();
    }

    @Test
    void createLog() {
        Log l = new Log();
        l = logService.create(l);

        l = new Log("TEST", LogType.ERROR);
        l = logService.create(l);
        assertEquals("TEST", l.getMessage());
        assertEquals(LogType.ERROR, l.getType());
        assertNotEquals(null, l.getTimestamp());
    }

    @Test
    void countTest() {
        Log l = new Log("TEST", LogType.ERROR);
        l = logService.create(l);

        l = new Log("TEST2", LogType.ERROR);
        l = logService.create(l);

        long cnt = logService.getCount();
        assertEquals(2, cnt);
    }

    @Test
    void listTest() {
        Log l = new Log("TEST", LogType.ERROR);
        l = logService.create(l);

        l = new Log("TEST2", LogType.ERROR);
        l = logService.create(l);

        List<Log> lstLogs = logService.list();
        assertEquals(2, lstLogs.size());
    }
}