package com.example.socialmediaproject.api;

import android.util.Log;

import com.example.socialmediaproject.adapters.UserAdapter;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/9/21.
 */

public class PostHelper {

    private static final String COLLECTION_NAME = "posts";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getPostCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<DocumentReference> createPostForGroup(Post postToCreate){
        return PostHelper.getPostCollection().add(postToCreate); // set :  Crée (ou écrase) un Document à partir d'un objet POJO.
    }


    // --- GET ---
    public static Query getAllPostForGroup(String groupName){
        return PostHelper.getPostCollection()
                .whereEqualTo("group", groupName)
                .orderBy("dateCreated",Query.Direction.DESCENDING)
                .limit(50);
    }


    // --- GET ---
    public static Query getAllPost(){
        return PostHelper.getPostCollection()
                .orderBy("dateCreated", Query.Direction.DESCENDING);
    }

    public static Query getAllUsersNumbersForGroup(String groupName) {
        return PostHelper.getPostCollection()
                .whereEqualTo("group", groupName)
                .orderBy("dateCreated", Query.Direction.ASCENDING)
                .limit(50);
    }

    // --- GET ---
    public static Query getAllMyPosts(String uid){
        return PostHelper.getPostCollection()
                .whereEqualTo("userSender", uid)
                .orderBy("dateCreated", Query.Direction.DESCENDING);
    }



    public static Query getMessageFromGroup(String name){
        return PostHelper.getPostCollection()
                .whereEqualTo("group",name)
                .orderBy("dateCreated");
    }


    // --- DELETE ---
    public static Task<Void> deletePost(String postId) {
        return PostHelper.getPostCollection().document(postId).delete();
    }
}