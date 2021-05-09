package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Message;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

/**
 * Created by Antoine Barbier on 5/9/21.
 */
public class MessageHelper {
    private static final String COLLECTION_NAME = "messages";

    // --- CREATE ---
    public static Task<DocumentReference> createMessageForChat(String textMessage, String chat, User userSender){

        // 1 - Create the Message object
        Message message = new Message(textMessage, userSender);

        // 2 - Store Message to Firestore
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message);
    }

    // --- GET ---
    public static Query getAllMessageForChat(String chat){
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }
}
