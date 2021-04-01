package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class PostItem {

    // fields
    private String title;
    private String subtitle;
    private String content;
    private String date;
    private String nbViews;
    private String nbStars;


    // constructor
    public PostItem(String title){
        this.title = title;
        this.content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat " +
                "non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        this.subtitle = "Inconnu";

        // Générer des nombres random pour le template
        int randomView = (int) (Math.random() * 1000);
        this.nbViews = ""+ randomView ;
        this.nbStars = ""+  ( Math.abs((int)(Math.random() * randomView)));

    }

    // methods
    public String getTitle(){ return this.title;}
    public String getSubtitle(){ return this.subtitle;}
    public String getContent(){ return this.content;}
    public String getNbViews(){ return this.nbViews;}
    public String getNbStars(){ return  this.nbStars;}
}
