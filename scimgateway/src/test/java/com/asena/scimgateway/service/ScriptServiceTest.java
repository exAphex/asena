package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.Script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScriptServiceTest {

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private AttributeService attributeService;

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

    @Test
    void updateTest() {
        Script s = new Script();
        s.setName("test");
        s.setContent("testcontent");
        s = scriptService.create(s);
        
        s.setContent("testcontent1");
        s.setName("test1");
        s = scriptService.update(s, s.getId());

        assertEquals("testcontent1", s.getContent());
        assertEquals("test", s.getName());

        assertThrows(NotFoundException.class, () -> {
            scriptService.update(null, 0);
        });
    }

    @Test
    void listAllTest() {
        Script s = new Script();
        Script sTwo = new Script();
        s.setName("test");
        s.setContent("testcontent");
        sTwo.setName("test2");
        sTwo.setContent("testcontent2");
        s = scriptService.create(s);
        sTwo = scriptService.create(sTwo);

        List<Script> scripts = scriptService.list();

        assertEquals(2, scripts.size());
    }

    @Test
    void deleteTest() {
        Script s = new Script();
        Script sTwo = new Script();
        s.setName("test");
        s.setContent("testcontent");
        s = scriptService.create(s);
        sTwo.setName("test2");
        sTwo.setContent("testcontent2");
        sTwo = scriptService.create(sTwo);

        Attribute a = new Attribute();
        a.setTransformation(s);
        a = attributeService.create(a);

        scriptService.deleteById(s.getId());

        assertThrows(NotFoundException.class, () -> {
            scriptService.deleteById(0);
        });

        List<Script> scripts = scriptService.list();
        assertEquals(1, scripts.size());

        scriptService.deleteAll();
        scripts = scriptService.list();
        assertEquals(0, scripts.size());
    }
}