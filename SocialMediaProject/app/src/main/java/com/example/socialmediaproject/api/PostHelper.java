package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Message;
import com.example.socialmediaproject.models.Notif;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class PostHelper {

    private static final String COLLECTION_NAME = "posts";

    // --- CREATE ---
    public static Task<DocumentReference> createPostForGroup(String content, String groupName){
        // 1 - Create the Message object
        Post post = new Post(content);
        // 2 - Store Message to Firestore
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .collection(COLLECTION_NAME)
                .add(post);
    }

    // --- GET ---
    public static Query getAllPostForGroup(String name){
        return GroupHelper.getGroupCollection()
                .document(name)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }

    // --- DELETE ---
    public static Task<Void> deletePost(String groupId, String postId) {
        return GroupHelper.getGroupCollection().document(groupId)
                .collection(COLLECTION_NAME).document(postId).delete();
    }
}