package com.example.socialmediaproject.models;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class GroupItem {

    // fields
    private String name;   // titre du groupe
    private String type; // type du groupe
    private String field; // domaine du groupe
    private String admin; // nom de l'admin

    private Boolean is_private; // if private : true, else is public

    // constructor
    public GroupItem(String _name, String _type, Boolean _private){
        this.type = _type;
        this.name = _name;
        this.field = "sujet inconnu";
        this.admin ="inconnu";
        this.is_private = _private;
    }

    // methods
    public String getName(){ return this.name;}
    public String getType() { return this.type; }
    public String getAdmin(){ return this.admin;}
    public String getField(){ return this.field;}

    public Boolean isPrivate(){
        return this.is_private;
    }
}
