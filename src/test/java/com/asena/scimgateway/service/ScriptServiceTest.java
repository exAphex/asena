package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.asena.scimgateway.model.Script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScriptServiceTest {

    @Autowired
    private ScriptService scriptService;

    @BeforeEach
    void prepareDB() {
        scriptService.deleteAll();
    }

    @Test
    void createTest() {
        Script s = new Script();
        s.setName("test");
        s.setContent("testcontent");

        s = scriptService.create(s);
        assertEquals("test", s.getName());
        assertEquals("testcontent", s.getContent());
    }
}