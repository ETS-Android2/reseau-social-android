package com.example.socialmediaproject.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Antoine Barbier on 5/9/21.
 */

public class User implements Serializable {

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
        this.urlPicture = "gs://social-media-project-f6ca2.appspot.com/images/default.png";
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