package com.example.socialmediaproject.models;

import java.io.Serializable;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class Notif implements Serializable {

    private String type;    // type de notification
    private String title;   // titre de la notification
    private String content; // contenu du message de la notification


    // --- CONSTRUCTORS ---
    public Notif(String title, String content){
        this.type = "default";
        this.title = title;
        this.content = content;
    }

    public Notif(String title){
        this.type = "default";
        this.title = title;
        this.content = "Le contenu du message n'est pas spécifié";
    }

    // --- GETTERS ---
    public String getType(){ return this.type;}
    public String getTitle(){ return this.title;}
    public String getContent(){ return this.content;}

    // --- SETTERS ---
    public void setType(String type){ this.type = type;}
    public void setTitle(String title){ this.title = title;}
    public void setContent(String content){ this.content = content;}
}
