package com.example.socialmediaproject.models;


import java.io.Serializable;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */

public class Group implements Serializable {

    private String name, type, field;
    private String admin;
    //private Publication publication;
    //private Access access;
    private Boolean is_private; // if private : true, else is public
    


    //  --- CONSTRUCTORS ---
    public Group(){
        this.type   = "type";
        this.name   = "name";
        this.field  = "field";
        this.admin  = "idAdmin";
        //this.access = _access;
        //this.publication = Publication.ALL;
        this.is_private = true; //this.access.equals(Access.PRIVATE);
    }
    public Group(String _name, String _type, String _field, String _admin){
        this.type   = _type;
        this.name   = _name;
        this.field  = _field;
        this.admin  = _admin;
        //this.access = _access;
        //this.publication = Publication.ALL;
        this.is_private = true; //this.access.equals(Access.PRIVATE);

    }

    // --- GETTERS ---
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public String getAdmin(){ return this.admin;}
    //public Access getAccess(){ return this.access;}
    //public Publication getPublication(){ return this.publication;}

    //public List<Post> getPosts(){ return this.postList; }
    //public List<User> getMembers(){ return this.membersList; }


    // --- SETTERS ---
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(String admin){ this.admin = admin;}
    //public void setAccess(Access access){ this.access = access;}


    // --- METHODS ---
    public Boolean isPrivate(){  return this.is_private; }
}
