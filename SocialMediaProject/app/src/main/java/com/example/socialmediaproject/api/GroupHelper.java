package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Group;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
    public static Task<Void> createGroup(Group groupToCreate) {
        return GroupHelper.getGroupCollection()
                .document(groupToCreate.getName())
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
    // -- Gestion des adhérants dans le groupe --
    public static Task<Void> remoreUserFromGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("members", FieldValue.arrayRemove(uid),
                        "moderators",FieldValue.arrayRemove(uid));
    }

    public static Task<Void> addUserInGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("members", FieldValue.arrayUnion(uid));
    }

    public static Task<Void> promoteMemberToModerator(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("moderators", FieldValue.arrayUnion(uid));
    }

    public static Task<Void> demoteModeratorToMember(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("moderators", FieldValue.arrayRemove(uid));
    }


    // --- DELETE ---
    public static Task<Void> deleteGroup(String id) {
        return GroupHelper.getGroupCollection().document(id).delete();
    }
}
