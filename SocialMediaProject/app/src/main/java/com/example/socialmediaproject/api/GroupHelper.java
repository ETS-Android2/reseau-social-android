package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by Antoine Barbier and Antoine Brahimi  on 5/9/21.
 */
public class GroupHelper {
    private static final String COLLECTION_NAME = "groups";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getGroupCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createGroup(String name, String type, String field, DocumentReference admin) {
        // 1 - Create User object
        Group groupToCreate = new Group(name, type, field, admin);
        // 2 - Add a new User Document to Firestore
        return GroupHelper.getGroupCollection()
                .document(name)
                .set(groupToCreate); // set :  Crée (ou écrase) un Document à partir d'un objet POJO.
    }

    // --- GET ALL GROUP ---
    public static Query getAllGroup(){
        return GroupHelper.getGroupCollection();
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getGroup(String groupName){
        return GroupHelper.getGroupCollection().document(groupName).get();
    }

    // --- GET BY TYPE---
    public static Query getAllGroupByType(String type){
        return GroupHelper.getGroupCollection()
                .whereEqualTo("type", type);

    }

    // --- UPDATE ---


    // --- DELETE ---
    public static Task<Void> deleteGroup(String id) {
        return GroupHelper.getGroupCollection().document(id).delete();
    }
}
