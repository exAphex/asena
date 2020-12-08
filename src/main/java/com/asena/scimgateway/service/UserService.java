package com.asena.scimgateway.service;

import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.User;
import com.asena.scimgateway.repository.UserRepository;
import com.asena.scimgateway.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User createServiceUser(String prefix) {
        User u = new User();
        u.setUserName(prefix + "_COMM");
        u.setPassword(PasswordUtil.generatePassword(8));
        u.setActive(false);
        return create(u);
    }

    public User create(User u) {
        return userRepository.save(u);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User deleteById(long id) {
        return findById(id)
        .map(u -> {
            userRepository.deleteById(id);
            return u;
        })
        .orElseThrow(() -> new NotFoundException(id)); 
    }
}