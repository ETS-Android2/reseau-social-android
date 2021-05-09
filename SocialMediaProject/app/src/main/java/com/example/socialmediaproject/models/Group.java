package com.example.socialmediaproject.models;

import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.enums.Publication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */

public class Group implements Serializable {

    private String name, type, field;
    private User admin;
    private Publication publication;
    private Access access;
    private Boolean is_private; // if private : true, else is public

    private List<Post> postList;

    private List<User> membersList;


    //  --- CONSTRUCTORS ---
    public Group(String _name, String _type, String _field, User _admin, Access _access){
        this.type   = _type;
        this.name   = _name;
        this.field  = _field;
        this.admin  = _admin;
        this.access = _access;
        this.publication = Publication.ALL;
        this.is_private = this.access.equals(Access.PRIVATE);

        this.membersList = new ArrayList<>();
        this.membersList.add(new User("Antoine Barbier"));
        this.membersList.add(new User("Antoine Brahimi"));
        this.membersList.add(new User("Thomas Pesquet"));
        this.membersList.add(new User("Usain Bolt"));
        this.membersList.add(new User("Andrés Pérès"));

        this.postList = new ArrayList<>();
        this.postList.add(new Post(this, membersList.get(0)));
        this.postList.add(new Post(this, membersList.get(1)));
        this.postList.add(new Post(this, membersList.get(2)));
        this.postList.add(new Post(this, membersList.get(3)));
        this.postList.add(new Post(this, membersList.get(4)));


    }

    // --- GETTERS ---
    public String getName(){ return this.name;}
    public String getType() { return this.type;}
    public String getField() { return this.field;}
    public User getAdmin(){ return this.admin;}
    public Access getAccess(){ return this.access;}
    public Publication getPublication(){ return this.publication;}

    public List<Post> getPosts(){ return this.postList; }
    public List<User> getMembers(){ return this.membersList; }


    // --- SETTERS ---
    public void setName(String name){ this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setField(String field) { this.field = field;}
    public void setAdmin(User admin){ this.admin = admin;}
    public void setAccess(Access access){ this.access = access;}


    // --- METHODS ---
    public Boolean isPrivate(){  return this.is_private; }
}
