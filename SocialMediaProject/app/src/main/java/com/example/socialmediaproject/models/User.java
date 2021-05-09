package com.example.socialmediaproject.models;

import androidx.annotation.Nullable;

/**
 * Created by Antoine Barbier on 5/9/21.
 */

public class User {

    private String uid;
    private String username;
    private String phoneNumber;
    private String email;
    @Nullable private String urlPicture;

    // --- CONSTRUCTORS ---
    public User() { }

    public User(String username) {
        this.uid = "uid";
        this.username = username;
        this.phoneNumber = "phone";
        this.email = "email";
        this.urlPicture = "urlPicture";
    }

    public User(String uid, String username, String phone, String email) {
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phone;
        this.email = email;
        this.urlPicture = "null";
    }

    public User(String uid, String username, String phone, String email, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phone;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getUrlPicture() { return urlPicture; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setPhoneNumber(String phone) { this.phoneNumber =  phone; }
    public void setEmail(String email) { this.email =  email; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
}