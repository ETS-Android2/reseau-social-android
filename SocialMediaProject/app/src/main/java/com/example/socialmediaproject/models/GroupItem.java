package com.example.socialmediaproject.models;

import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.enums.Publication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */

public class GroupItem  implements Serializable {
    // fields
    private String name, type, field, admin;
    private Publication publication;
    private Access access;
    private Boolean is_private; // if private : true, else is public

    private List<PostItem> postList;

    private List<UserHelperClass> membersList;


    // constructor
    public GroupItem(String _name, String _type, String _field, String _admin,  Access _access){
        this.type   = _type;
        this.name   = _name;
        this.field  = _field;
        this.admin  = _admin;
        this.access = _access;
        this.publication = Publication.ALL;
        this.is_private = this.access.equals(Access.PRIVATE);

        this.postList = new ArrayList<>();
        this.postList.add(new PostItem(this, "Antoine"));
        this.postList.add(new PostItem(this, "Thomas"));
        this.postList.add(new PostItem(this, "Enzo"));
        this.postList.add(new PostItem(this, "Pedro"));
        this.postList.add(new PostItem(this, "José"));

        this.membersList = new ArrayList<>();
        this.membersList.add(new UserHelperClass("Antoine Barbier"));
        this.membersList.add(new UserHelperClass("Antoine Brahimi"));
        this.membersList.add(new UserHelperClass("Thomas Pesquet"));
        this.membersList.add(new UserHelperClass("Usain Bolt"));
        this.membersList.add(new UserHelperClass("Andrés Pérès"));
    }

    // methods get
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public String getAdmin(){ return this.admin;}
    public Access getAccess(){ return this.access;}
    public Publication getPublication(){ return this.publication;}

    public List<PostItem> getPosts(){ return this.postList; }
    public List<UserHelperClass> getMembers(){ return this.membersList; }

    // methods set
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(String admin){ this.admin = admin;}
    public void setAccess(Access access){ this.access = access;}


    public Boolean isPrivate(){  return this.is_private; }
}
