package com.asena.scimgateway.controller;

import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.dto.UserDTO;
import com.asena.scimgateway.security.stereotypes.CurrentUser;
import com.asena.scimgateway.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("")
    public UserDTO updateScript(@CurrentUser User currUser, @RequestBody UserDTO userDTO) {
        User usr = userDTO.fromDTO();
        return UserDTO.toDTO(userService.updateAdminUser(usr, currUser.getId()));
    }
}