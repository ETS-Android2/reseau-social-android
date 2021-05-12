package com.example.socialmediaproject.api;

import androidx.annotation.NonNull;

import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createUser(String uid, String username, String phone, String email, String urlPicture) {
        // 1 - Create User object
        User userToCreate = new User(uid, username, phone, email, urlPicture);
        // 2 - Add a new User Document to Firestore
        return UserHelper.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }

    // --- ADD USER TO A GROUP ---
    public static Task<DocumentReference> addUserInGroup(){
        // 1 - Create the Message object
        User user = new User("antoine");
        // 2 - Store Message to Firestore
        return GroupHelper.getGroupCollection()
                .document("test")
                .collection(COLLECTION_NAME)
                .add(user);
    }


    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}