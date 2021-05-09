package com.example.socialmediaproject.api;

import com.google.firebase.firestore.Query;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class PostHelper {

    private static final String COLLECTION_NAME = "posts";

    // --- GET ---
    public static Query getAllMessageForGroup(String group){
        return GroupHelper.getGroupCollection()
                .document(group)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }
}