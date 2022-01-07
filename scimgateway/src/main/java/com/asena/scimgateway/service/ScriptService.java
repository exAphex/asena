package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.repository.AttributeRepository;
import com.asena.scimgateway.repository.ScriptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    public List<Script> list() {
        return scriptRepository.findAll();
    }

    public Optional<Script> findById(long id) {
        return scriptRepository.findById(id);
    }

    public Script create(Script sc) {
        Script s = new Script();
        s.setName(sc.getName());
        s.setContent(sc.getContent());
        return scriptRepository.save(s);
    }

    public Script update(Script sc, long id) {
        return findById(id).map(s -> {
            s.setContent(sc.getContent());

            return scriptRepository.save(s);
        }).orElseThrow(() -> new NotFoundException(id));
    }

    public Script deleteById(long id) {
        return findById(id).map(sc -> {
            deleteScript(sc);
            return sc;
        }).orElseThrow(() -> new NotFoundException(id));
    }

    public void deleteAll() {
        List<Script> scripts = scriptRepository.findAll();
        for (Script s : scripts) {
            deleteScript(s);
        }
    }

    private void deleteScript(Script s) {
        List<Attribute> attrs = attributeRepository.findByTransformationId(s.getId());
        for (Attribute a : attrs) {
            a.setTransformation(null);
            a = attributeRepository.save(a);
        }

        scriptRepository.deleteById(s.getId());
    }

    public Script findByName(String name) {
        return scriptRepository.findByName(name);
    }
}