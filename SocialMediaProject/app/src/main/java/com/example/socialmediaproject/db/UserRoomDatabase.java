package com.example.socialmediaproject.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.socialmediaproject.db.dao.UserDao;
import com.example.socialmediaproject.db.entities.UserEntity;

/*
*   Configuration de la BDD user
*/

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserRoomDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile UserRoomDatabase userRoomDatabase;

    public static UserRoomDatabase getDatabase(final Context context){
        if(userRoomDatabase == null){
            synchronized (UserRoomDatabase.class){
                if(userRoomDatabase == null){
                    userRoomDatabase = Room.databaseBuilder(context.getApplicationContext(), UserRoomDatabase.class, "user_database").allowMainThreadQueries().build();
                }
            }
        }
        return userRoomDatabase;
    }
}
