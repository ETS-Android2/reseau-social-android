package com.example.socialmediaproject.models;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class PostItem implements Serializable {

    // fields
    private String id;
    private GroupItem group;
    private String author, content;
    private String media;
    private Date date;
    private int nbViews;
    private int nbStars;

    private Boolean isLike;


    // constructor
    public PostItem(GroupItem group, String author){
        this.group = group;
        this.author = author;
        this.content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat " +
                "non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        this.date = new Date();
        // Générer des nombres random pour le template
        int randomView = (int) (Math.random() * 1000);
        this.nbViews = randomView ;
        this.nbStars =  ( Math.abs((int)(Math.random() * randomView)));

        this.isLike = false;

    }

    // methods
    public String getId(){ return this.id;}
    public GroupItem getGroup(){ return this.group;}
    public String getAuthor(){ return this.author;}
    public String getContent(){ return this.content;}
    public Date getDate(){ return this.date;}

    public int getNbViews(){ return this.nbViews;}
    public int getNbStars(){ return  this.nbStars;}
    public Boolean getIsLike(){ return  this.isLike;}

    public void setGroup(GroupItem group){  this.group = group;}
    public void setAuthor(String author){ this.author = author;}
    public void setContent(String content){ this.content = content;}

    public String getTimeAgo(){
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            //Date past = format.parse("27/04/2021");
            Date past = this.date;
            Date now = new Date();
            if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 60){
                if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 1){
                    return "maintenant";
                }else{
                    return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " min";
                }

            }else{
                if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) < 24){
                    return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " h";
                }else{
                    if(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 1){
                        return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " jour";
                    }else{
                        return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " jours";
                    }

                }
            }

        }
        catch (Exception j){
            j.printStackTrace();
            return "error";
        }
    }
    public void changeLike(){
        this.isLike = !this.isLike;
        if(this.isLike){
            this.nbStars +=1; // donc on rajoute une étoile
        }else{
            this.nbStars -=1; // donc on enlève une étoile
        }
    }

}
