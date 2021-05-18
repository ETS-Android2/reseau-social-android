package com.example.socialmediaproject.models;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class Post implements Serializable {

    private String content;
    private String group;
    private String userSender;
    private Date dateCreated;
    private String urlImage;

    public Post(){
        this.content = "content";
        this.group = "content";
        this.userSender = "content";
        this.urlImage = null;
    }
    public Post(String content, String group, String userSender, String urlPicture){
        this.content = content;
        this.group = group;
        this.userSender = userSender;
        this.urlImage = urlPicture;
    }

    // --- GETTERS ---
    public String getContent(){ return this.content;}
    public String getGroup(){ return this.group;}
    public String getUserSender(){ return this.userSender;}
    @ServerTimestamp public Date getDateCreated(){ return this.dateCreated;}
    public String getUrlImage() { return urlImage; }

    // --- SETTERS ---
    public void setContent(String content){ this.content = content;}
    public void setGroup(String group){  this.group = group;}
    public void setUserSender(String userSender) { this.userSender = userSender; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

}
