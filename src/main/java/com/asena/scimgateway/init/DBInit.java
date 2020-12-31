package com.asena.scimgateway.init;

import javax.annotation.PostConstruct;

import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.User.UserType;
import com.asena.scimgateway.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInit {
    
    @Autowired
    private UserService userService;

    @PostConstruct
    private void initDB() {
        User u = userService.findByUserName("admin");
        if (u == null) {
            u = new User();
            u.setUserName("admin");
            u.setPassword("admin");
            u.setType(UserType.ADMIN);
            u.setMail("admin@adminmail.com");
            userService.create(u);
        }
    }
}