package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/31/21.
 */
public class ProfileItem {
    // fields
    private String title;   // titre du groupe
    private String mnenonic;

    // constructor
    public ProfileItem(String title, String mnenonic){
        this.title = title;
        this.mnenonic = mnenonic;
    }

    // methods
    public String getTitle(){ return this.title;}
    public String getMnenonic() {
        return mnenonic;
    }
}
