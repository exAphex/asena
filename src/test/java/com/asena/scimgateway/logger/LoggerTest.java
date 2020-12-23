package com.asena.scimgateway.logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.model.Log.LogType;
import com.asena.scimgateway.service.LogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoggerTest {
    @Autowired
    private Logger logger;

    @Autowired
    private LogService logService;

    @BeforeEach
    void prepareDb() {
        logService.deleteAll();
    }

    @Test
    void setLogLevelTest() {
        logger.setLogLevel(LogType.DEBUG);
        assertEquals(LogType.DEBUG, logger.getLogLevel());
    }

    @Test
    void testInfo() {
        logger.setLogLevel(LogType.INFO);
        logger.info("testinfolog");
        List<Log> logs = logService.list();
        assertEquals(1, logs.size());
        assertEquals("testinfolog", logs.get(0).getMessage());
        assertEquals(LogType.INFO, logs.get(0).getType());

        logger.debug("testdebuglog");
        logger.warning("testwarnlog");
        logger.error("testerrlog");
        logger.error(new Exception("testerr"));

        logs = logService.list();
        assertEquals(6, logs.size());
    }

    @Test
    void testDebug() {
        logger.setLogLevel(LogType.DEBUG);
        logger.info("testinfolog");
        logger.debug("testdebuglog");
        List<Log> logs = logService.list();
        assertEquals(1, logs.size());
        assertEquals("testdebuglog", logs.get(0).getMessage());
        assertEquals(LogType.DEBUG, logs.get(0).getType());

        logger.warning("testwarnlog");
        logger.error("testerrlog");
        logger.error(new Exception("testerr"));

        logs = logService.list();
        assertEquals(5, logs.size());
    }

    @Test
    void testWarning() {
        logger.setLogLevel(LogType.WARNING);
        logger.info("testinfolog");
        logger.debug("testdebuglog");
        logger.warning("testwarnlog");

        List<Log> logs = logService.list();
        assertEquals(1, logs.size());
        assertEquals("testwarnlog", logs.get(0).getMessage());
        assertEquals(LogType.WARNING, logs.get(0).getType());

        logger.error("testerrlog");
        logger.error(new Exception("testerr"));

        logs = logService.list();
        assertEquals(4, logs.size());
    }

    @Test
    void testError() {
        logger.setLogLevel(LogType.ERROR);
        logger.info("testinfolog");
        logger.debug("testdebuglog");
        logger.warning("testwarnlog");
        logger.error("testerrlog");

        final List<Log> logs = logService.list();
        assertEquals(1, logs.size());
        assertEquals("testerrlog", logs.get(0).getMessage());
        assertEquals(LogType.ERROR, logs.get(0).getType());
    }

    @Test
    void testErrorException() {
        Exception e = null;
        logger.setLogLevel(LogType.ERROR);
        logger.error(e);

        List<Log> logs = logService.list();
        assertEquals(0, logs.size());

        e = new Exception("TESTERR");
        logger.error(e);
        logs = logService.list();
        assertEquals(2, logs.size());
    }

    @Test
    void testErrorInternalErrorException() {
        InternalErrorException e = new InternalErrorException("TESTERR");
        logger.setLogLevel(LogType.ERROR);
        logger.error(e);

        List<Log> logs = logService.list();
        assertEquals(2, logs.size());
    }

    @Test
    void testErrorInternalErrorExceptionWithObject() {
        InternalErrorException e = new InternalErrorException("TESTERR");
        e.setObj("TEST");
        logger.setLogLevel(LogType.ERROR);
        logger.error(e);

        List<Log> logs = logService.list();
        assertEquals(3, logs.size());
    }


    @Test
    void testNone() {
        logger.setLogLevel(LogType.NONE);
        logger.info("testinfolog");
        logger.debug("testdebuglog");
        logger.warning("testwarnlog");
        logger.error("testerrlog");
        logger.error(new Exception("testexceptionlog"));

        final List<Log> logs = logService.list();
        assertEquals(0, logs.size());
    }
}