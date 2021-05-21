package com.example.socialmediaproject.models;


import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */

public class Group implements Serializable {

    private String name, type, field;
    private String admin;
    private Boolean publicationOnlyModerator;
    private Boolean accessPrivate;

    // cet attribut est le nom du groupe écrit en minuscule pour pouvoir faire une recherche sans se soucier des majuscule
    private String search;

    // Les user qui sont en attente d'acceptation
    List<String> waitlist;

    List<String> members;
    List<String> moderators;

    //  --- CONSTRUCTORS ---
    public Group(){
        this.type   = "post";
        this.name   = "name";
        this.search = this.name.toLowerCase();
        this.field  = "field";
        this.admin  = "idAdmin";
        this.publicationOnlyModerator = false;
        this.accessPrivate =true;

        this.members = Arrays.asList(this.admin);
        this.moderators = Arrays.asList(this.admin);
        this.waitlist =  Arrays.asList();
    }
    public Group(String _name, String _type, String _field, String access, String _admin){
        this.type   = _type;
        this.name   = _name;
        this.search = this.name.toLowerCase();
        this.field  = _field;
        this.admin  = _admin;

        // Pour les groupes de type sms et email, seul l'admin peut publié
        switch (_type){
            case "email":
            case "sms":
                this.publicationOnlyModerator = true;
                break;
            case "post":
            case "chat":
            default:
                this.publicationOnlyModerator = false;
                break;
        }

        this.accessPrivate = access.equals("private"); // alors true sinon public
        this.members = Arrays.asList(this.admin);
        this.moderators = Arrays.asList(this.admin);

        this.waitlist =  Arrays.asList();
    }


    // --- GETTERS ---
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public String getAdmin(){ return this.admin;}
    public Boolean getAccessPrivate(){return this.accessPrivate; }
    public Boolean getPublicationOnlyModerator(){ return this.publicationOnlyModerator;}
    public String getSearch() { return search; }

    public List<String> getMembers(){ return this.members; }
    public List<String> getModerators(){ return this.moderators; }

    public List<String> getWaitlist() { return this.waitlist; }




    // --- SETTERS ---
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(String admin){ this.admin = admin;}
    public void setAccessPrivate(Boolean access){ this.accessPrivate = access;}
    public void setPublicationOnlyModerator(Boolean publicationOnlyModerator) { this.publicationOnlyModerator = publicationOnlyModerator; }
    public void setSearch(String search) { this.search = search; }
    // --- METHODS ---
    //public Boolean isPrivate(){  return this.accessPrivate; }

    public void addToModerators(String uid){ this.moderators.add(uid);}
    public void removeToModerators(String uid){ this.moderators.remove(uid);}

    public void addToMembers(String uid){ this.members.add(uid);}
    public void removeToMembers(String uid){ this.members.remove(uid);}

    public void removeFromWaitlist(String uid){ this.waitlist.remove(uid);}
}
