package com.example.socialmediaproject.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * Modele utilisé lors du stockage d'un utilisateur
 * dans la BDD locale (Room database)
 */

@Entity(tableName = "User")
public class UserEntity {

    @PrimaryKey
    @NonNull
    public String id; // Correspond à l'id de l'utilisateur dans la BDD distante (Firebase)

    @ColumnInfo(name = "email")
    @NonNull
    public String email;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "password")
    @NonNull
    public String password;

    @ColumnInfo(name = "phone")
    @NonNull
    public String phone;

    public UserEntity(String id, String email, String name, String password, String phone){
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }
}
