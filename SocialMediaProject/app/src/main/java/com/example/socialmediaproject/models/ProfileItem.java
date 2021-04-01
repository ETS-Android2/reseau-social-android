package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier on 3/31/21.
 */
public class ProfileItem {
    // fields
    private String title;   // titre du groupe

    // constructor
    public ProfileItem(String title){
        this.title = title;
    }

    // methods
    public String getTitle(){ return this.title;}
}
