package com.asena.scimgateway.script;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Script;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScriptRunnerTest {
    
    @Test
    void scriptRunnerTest() {
        ScriptRunner sr = new ScriptRunner();
        Script s = new Script();
        String param = "Test";
        s.setName("warning");
        s.setContent("function warning(param) { return param }");
        sr.addScriptFunction(s);
        sr.addScriptFunction(null);
        String retStr = (String)sr.execute(s, param);
        assertEquals(param, retStr);
    }

    @Test
    void scriptRunnerFailTest() {
        ScriptRunner sr = new ScriptRunner();
        String param = "Test";
        Script s = new Script();
        String retStr = (String)sr.execute(null, param);

        assertEquals(param, retStr);

        retStr = (String)sr.execute(s, param);
        assertEquals(retStr, param);

        s.setName("Test");
        retStr = (String)sr.execute(s, param);
        assertEquals(retStr, param);

        s.setContent("function warning(param) { return param }");
        assertThrows(InternalErrorException.class, () -> {
            sr.execute(s, param);
        });
    }
}