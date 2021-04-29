package com.example.socialmediaproject.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.socialmediaproject.db.entities.UserEntity;

import java.util.List;

/*
* UserDao fournit les méthodes permettant au reste de l'application
* d'interagir avec les données de la table User Entity
*/

@Dao
public interface UserDao {

    @Insert
    public void insert(UserEntity user);

    @Update
    public void update(UserEntity user);

    @Delete
    public void delete(UserEntity user);

    @Query("SELECT * FROM User")
    public List<UserEntity> getAll();
}
