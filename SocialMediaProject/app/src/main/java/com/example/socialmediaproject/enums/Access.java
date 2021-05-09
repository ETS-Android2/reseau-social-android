package com.example.socialmediaproject.enums;

/**
 * Created by Antoine Barbier on 4/28/21.
 */
public enum Access {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    public final String label;

    private Access(String label){
        this.label = label;
    }
};