package com.asena.scimgateway.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.asena.scimgateway.security.converter.AttributeEncrypter;

@Entity
@Table(name = "users")
public class User {

    public enum UserType {
        ADMIN, TECHNICAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "Username is mandatory")
    private String userName;
    @NotBlank(message = "Username is mandatory")
    @Convert(converter = AttributeEncrypter.class)
    private String password;
    private String mail;

    @Enumerated(EnumType.ORDINAL)
    private UserType type;
    

    public long getId() {
        return id;
    }

    public UserType getType() {
        return this.type;
    }

    public void setType(UserType type) {
        this.type = type;
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