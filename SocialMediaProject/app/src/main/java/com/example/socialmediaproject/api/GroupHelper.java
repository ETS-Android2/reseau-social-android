package com.example.socialmediaproject.api;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Antoine Barbier and Antoine Brahimi  on 5/9/21.
 */
public class GroupHelper {
    private static final String COLLECTION_NAME = "groups";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getGroupCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
}
