package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class NotifItem {

    // fields
    private String type;    // type de notification
    private String title;   // titre de la notification
    private String content; // contenu du message de la notification


    // constructor
    public NotifItem(String title, String content){
        this.type = "default";
        this.title = title;
        this.content = content;
    }
    // constructor
    public NotifItem(String title){
        this.type = "default";
        this.title = title;
        this.content = "Le contenu du message n'est pas spécifié";
    }

    // methods
    public String getType(){ return this.type;}
    public String getTitle(){ return this.title;}
    public String getContent(){ return this.content;}

}
