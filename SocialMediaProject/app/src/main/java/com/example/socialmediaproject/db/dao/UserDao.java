package com.example.socialmediaproject.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleRecord(UserEntity user);

    @Update
    public void update(UserEntity user);

    @Delete
    public void delete(UserEntity user);

    @Query("SELECT * FROM User WHERE uid LIKE :userID")
    UserEntity getUser(String userID);

    @Query("DELETE FROM User")
    public void deleteAll();

    @Query("SELECT * FROM User")
    public List<UserEntity> getAll();
}
