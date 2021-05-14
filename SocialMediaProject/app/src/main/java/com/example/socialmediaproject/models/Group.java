package com.example.socialmediaproject.models;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */

public class Group implements Serializable {

    private String name, type, field;
    private String admin;
    //private Publication publication;
    //private Access access;
    private Boolean is_private; // if private : true, else is publics

    // Les user qui sont en attente d'acceptation
    List<String> waitingList;

    List<String> members;
    List<String> moderators;

    //  --- CONSTRUCTORS ---
    public Group(){
        this.type   = "type";
        this.name   = "name";
        this.field  = "field";
        this.admin  = "idAdmin";
        //this.access = _access;
        //this.publication = Publication.ALL;
        this.is_private = true; //this.access.equals(Access.PRIVATE);

        this.members = Arrays.asList(this.admin);
        this.moderators = Arrays.asList(this.admin);
        this.waitingList =  Arrays.asList();
    }
    public Group(String _name, String _type, String _field, String _admin){
        this.type   = _type;
        this.name   = _name;
        this.field  = _field;
        this.admin  = _admin;
        //this.access = _access;
        //this.publication = Publication.ALL;
        this.is_private = true; //this.access.equals(Access.PRIVATE);
        this.members = Arrays.asList(this.admin);
        this.moderators = Arrays.asList(this.admin);

        this.waitingList =  Arrays.asList();
    }


    // --- GETTERS ---
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public String getAdmin(){ return this.admin;}

    public List<String> getMembers(){ return this.members; }
    public List<String> getModerators(){ return this.moderators; }

    public List<String> getWaitingList(){ return this.waitingList; }



    // --- SETTERS ---
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(String admin){ this.admin = admin;}


    // --- METHODS ---
    public Boolean isPrivate(){  return this.is_private; }

    public void addToModerators(String uid){ this.moderators.add(uid);}
    public void removeToModerators(String uid){ this.moderators.remove(uid);}

    public void addToMembers(String uid){ this.members.add(uid);}
    public void removeToMembers(String uid){ this.members.remove(uid);}
}
