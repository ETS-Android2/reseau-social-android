package com.example.socialmediaproject.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/16/21.
 */
public class CodeAccess {
    private String groupName; // nom du groupe d'accès
    private Boolean expired; // si le code à expriré
    private Date dateCreated; // date de création du code

    // --- CONSTRUCTOR ---
    public CodeAccess(){
        this.groupName = "";
        this.expired = false;
    }
    public CodeAccess(String groupName){
        this.groupName = groupName;
        this.expired = false;
    }

    // --- GETTERS ---
    public String getGroupName() { return groupName; }
    public Boolean getExpired() { return expired; }
    @ServerTimestamp public Date getDateCreated() { return dateCreated; }

    // --- SETTERS ---
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setExpired(Boolean expired) { this.expired = expired; }
}
