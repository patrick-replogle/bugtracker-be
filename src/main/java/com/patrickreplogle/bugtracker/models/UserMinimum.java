package com.patrickreplogle.bugtracker.models;

import javax.validation.constraints.Email;

public class UserMinimum {
    @Email
    private String email;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String company;
    private String imageurl;

    public UserMinimum() {
    }

    public UserMinimum(@Email String email, String username, String password, String firstname, String lastname, String company, String imageurl) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.imageurl = imageurl;
    }

    public UserMinimum(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
