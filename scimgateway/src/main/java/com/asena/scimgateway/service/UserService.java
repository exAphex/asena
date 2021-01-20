package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.User.UserType;
import com.asena.scimgateway.repository.RemoteSystemRepository;
import com.asena.scimgateway.repository.UserRepository;
import com.asena.scimgateway.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

    @Autowired
    private UserRepository userRepository;

    public User createServiceUser(String prefix) {
        User u = new User();
        u.setUserName(prefix + "_COMM");
        u.setPassword(PasswordUtil.generatePassword(8));
        u.setType(UserType.TECHNICAL);
        return create(u);
    }

    public User create(User u) {
        return userRepository.save(u);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    private void deleteUser(User u) {
        List<RemoteSystem> rs = remoteSystemRepository.findByServiceUserId(u.getId());
        for (RemoteSystem r : rs) {
            r.setServiceUser(null);
            r = remoteSystemRepository.save(r);
        }

        userRepository.deleteById(u.getId());
    }

    public void deleteAll() {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            deleteUser(u);
        }
    }

    public User deleteById(long id) {
        return findById(id)
        .map(u -> {
            userRepository.deleteById(id);
            return u;
        })
        .orElseThrow(() -> new NotFoundException(id)); 
    }

    public User updateAdminUser(User usr, long id) {
        return findById(id)
        .map(u -> {
            u.setPassword(usr.getPassword());
            return userRepository.save(u);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }
}