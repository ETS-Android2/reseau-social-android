package com.example.socialmediaproject.api;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Antoine Barbier on 5/9/21.
 */
public class ChatHelper {
    private static final String COLLECTION_NAME = "chats";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
}
