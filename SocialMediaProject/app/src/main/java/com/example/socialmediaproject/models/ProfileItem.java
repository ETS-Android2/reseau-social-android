package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/31/21.
 */
public class ProfileItem {

    private String title;
    private String mnenonic;

    // --- CONSTRUCTOR ---
    public ProfileItem(String title, String mnenonic){
        this.title = title;
        this.mnenonic = mnenonic;
    }

    // --- GETTERS ---
    public String getTitle(){ return this.title;}
    public String getMnenonic() {
        return mnenonic;
    }

    // --- SETTERS ---
    public void setTitle(String title){ this.title = title;}
    public void setMnenonic(String mnenonic) {  this.mnenonic = mnenonic; }
}
