package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class GroupItem {

    // fields
    private String title;   // titre du groupe

    // constructor
    public GroupItem(String title){
        this.title = title;
    }

    // methods
    public String getTitle(){ return this.title;}

}
