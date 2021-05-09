package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class UserHelperNewVersion {

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
        return UserHelperNewVersion.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelperNewVersion.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelperNewVersion.getUsersCollection().document(uid).update("username", username);
    }

    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {
        return UserHelperNewVersion.getUsersCollection().document(uid).delete();
    }

}