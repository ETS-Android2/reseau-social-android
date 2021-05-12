package com.example.socialmediaproject.models;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class Post implements Serializable {

    private String content;
    private String group;
    private String userSender;
    private Date dateCreated;
    private String urlImage;


    private int nbViews;
    private int nbStars;
    private Boolean isLike;


    public Post(){
        this.content = "content";
        this.group = "content";
        this.userSender = "content";
        this.urlImage = null;
        this.nbViews = 0;
        this.nbStars =  0;
        this.isLike = false;
    }

    public Post(String content){
        this.content = content;
        this.group = "content";
        this.userSender = "content";
        this.urlImage = null;
        this.nbViews = 0;
        this.nbStars =  0;
        this.isLike = false;
    }

    public Post(String content, String group, String userSender){
        this.content = content;
        this.group = group;
        this.userSender = userSender;
        this.urlImage = null;

        // Générer des nombres random pour le template
        int randomView = (int) (Math.random() * 1000);
        this.nbViews = randomView ;
        this.nbStars =  ( Math.abs((int)(Math.random() * randomView)));

        this.isLike = false;
    }

    // --- GETTERS ---
    public String getContent(){ return this.content;}
    public String getGroup(){ return this.group;}
    public String getUserSender(){ return this.userSender;}
    @ServerTimestamp public Date getDateCreated(){ return this.dateCreated;}
    public String getUrlImage() { return urlImage; }

    public Boolean getIsLike(){ return  this.isLike;}
    public int getNbViews(){ return this.nbViews;}
    public int getNbStars(){ return  this.nbStars;}

    // --- SETTERS ---
    public void setContent(String content){ this.content = content;}
    public void setGroup(String group){  this.group = group;}
    public void setUserSender(String userSender) { this.userSender = userSender; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }


    // --- METHODS ---
    public String getTimeAgo(){
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            //Date past = format.parse("27/04/2021");
            Date past = this.dateCreated;
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
