package com.asena.scimgateway.model.dto;

import com.asena.scimgateway.model.User;

public class UserDTO {
    private long id;
    private String userName;
    private String password;
    private String mail;
    private boolean active;

    public User fromDTO() {
        User u = new User();
        u.setId(this.id);
        u.setUserName(this.userName);
        u.setPassword(this.password);
        u.setMail(this.mail);
        u.setActive(this.active);
        return u;
    }

    public static UserDTO toDTO(User u) {
        UserDTO usrDTO = new UserDTO();
        usrDTO.setId(u.getId());
        usrDTO.setUserName(u.getUserName());
        usrDTO.setPassword(u.getPassword());
        usrDTO.setMail(u.getMail());
        usrDTO.setActive(u.isActive());
        return usrDTO;
    }

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(long id) {
        this.id = id;
    }
}