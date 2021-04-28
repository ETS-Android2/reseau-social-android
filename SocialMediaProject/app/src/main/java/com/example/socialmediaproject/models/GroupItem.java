package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class GroupItem {
    // fields
    private String name, type, field, admin, access, publication;
    private Boolean is_private; // if private : true, else is public

    // constructor
    public GroupItem(String _name, String _type, String _field, String _admin,  String _access){
        this.type   = _type;
        this.name   = _name;
        this.field  = _field;
        this.admin  = _admin;
        this.access = _access;
        this.publication = "all";
        this.is_private = this.access.equals("private");
    }

    // methods get
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public String getAdmin(){ return this.admin;}
    public String getAccess(){ return this.access;}
    public String getPublication(){ return this.publication;}

    // methods set
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(String admin){ this.admin = admin;}
    public void setAccess(String access){ this.access = access;}


    public Boolean isPrivate(){  return this.is_private; }
}
