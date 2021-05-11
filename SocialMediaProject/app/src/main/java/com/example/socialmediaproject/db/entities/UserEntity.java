package com.example.socialmediaproject.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.socialmediaproject.models.User;

import java.io.Serializable;

@Entity(tableName = "User")
public class UserEntity {

    @PrimaryKey
    @NonNull
    private String uid;

    @ColumnInfo(name = "username")
    @NonNull
    private String username;

    @ColumnInfo(name = "phone")
    @NonNull
    private String phoneNumber;

    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    @ColumnInfo(name = "urlPicture")
    @NonNull
    private String urlPicture;

    // --- CONSTRUCTORS ---
    public UserEntity() { }

    public UserEntity(String username) {
        this.uid = "uid";
        this.username = username;
        this.phoneNumber = "phone";
        this.email = "email";
        this.urlPicture = "urlPicture";
    }

    public UserEntity(String uid, String username, String phone, String email) {
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phone;
        this.email = email;
        this.urlPicture = "null";
    }

    public UserEntity(String uid, String username, String phone, String email, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phone;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    @NonNull
    public String getUid() { return uid; }
    @NonNull
    public String getUsername() { return username; }
    @NonNull
    public String getPhoneNumber() { return phoneNumber; }
    @NonNull
    public String getEmail() { return email; }
    @NonNull
    public String getUrlPicture() { return urlPicture; }

    public User getUser() {
        return new User(uid, username, phoneNumber, email, urlPicture);
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setPhoneNumber(String phone) { this.phoneNumber =  phone; }
    public void setEmail(String email) { this.email =  email; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
}