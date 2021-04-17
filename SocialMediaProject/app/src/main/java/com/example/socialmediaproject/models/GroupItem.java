package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class GroupItem {

    // fields
    private String title;   // titre du groupe
    private String type;

    // constructor
    public GroupItem(String title, String type){
        this.type = type;
        this.title = title;
    }

    // methods
    public String getTitle(){ return this.title;}

    public String getType() {
        return type;
    }
}
