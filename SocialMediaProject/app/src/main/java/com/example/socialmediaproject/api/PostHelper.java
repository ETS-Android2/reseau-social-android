package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class PostHelper {

    private static final String COLLECTION_NAME = "posts";

    // --- CREATE ---
    public static Task<DocumentReference> createPostForGroup(Post postToCreate){
        // 1 - Create the Message object

        // 2 - Store Message to Firestore
        return GroupHelper.getGroupCollection()
                .document(postToCreate.getGroup())
                .collection(COLLECTION_NAME)
                .add(postToCreate);
    }

    // --- GET ---
    public static Query getAllPostForGroup(String name){
        return GroupHelper.getGroupCollection()
                .document(name)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }

    // --- GET ---
    public static Query getAllPost(){
        return FirebaseFirestore.getInstance().collectionGroup("posts");
    }

    // --- DELETE ---
    public static Task<Void> deletePost(String groupId, String postId) {
        return GroupHelper.getGroupCollection().document(groupId)
                .collection(COLLECTION_NAME).document(postId).delete();
    }
}