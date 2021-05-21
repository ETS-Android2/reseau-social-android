package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.Group;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    // --- GET ALL PUBLIC GROUP ---
    public static Query getAllPublicGroup(){
        return GroupHelper.getGroupCollection()
               .whereEqualTo("accessPrivate", false);
    }

    // --- GET ALL PUBLIC GROUP BY TYPE ---
    public static Query getAllPublicGroupByType(String type){
        return GroupHelper.getGroupCollection()
                .whereEqualTo("type", type)
                .whereEqualTo("accessPrivate", false);
    }


    // --- GET ALL CURRENT USER ' S GROUP ---
    public static Query getAllGroup(String uid){
        return GroupHelper.getGroupCollection().whereArrayContains("members",uid);
    }

    // --- GET GROUP ---
    public static Task<DocumentSnapshot> getGroup(String groupName){
        return GroupHelper.getGroupCollection().document(groupName).get();
    }

    // --- GET GROUP ---
    public static DocumentReference getGroupRef(String groupName){
        return GroupHelper.getGroupCollection().document(groupName);
    }

    // --- GET GROUP BY TYPE---
    public static Query getAllGroupByType(String type, String uid){
        return GroupHelper.getGroupCollection()
                .whereEqualTo("type", type)
                .whereArrayContains("members",uid);
    }

    // --- GET ALL USER ADMIN GROUP  ---
    public static Query getAllMyGroupAdmin(String uid){
        return GroupHelper.getGroupCollection()
                .whereEqualTo("admin", uid);
    }



    // --- UPDATE ---
    // -- Gestion des adhérants dans le groupe --
    public static Task<Void> removeUserFromGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("members", FieldValue.arrayRemove(uid),
                        "moderators",FieldValue.arrayRemove(uid));
    }

    public static Task<Void> removeUserFromWaitlistGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("waitlist", FieldValue.arrayRemove(uid));
    }

    public static Task<Void> addUserInWaitlistGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("waitlist", FieldValue.arrayUnion(uid));
    }

    public static Task<Void> addUserInGroup(String groupName, String uid) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("members", FieldValue.arrayUnion(uid),"waitlist",FieldValue.arrayRemove(uid));
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

    public static DocumentReference getMembersListOfGroup(String groupName){
        return GroupHelper.getGroupCollection()
                .document(groupName);
    }

    // --- UPDATE Setting ---

    public static Task<Void> setGroupAccess(String groupName, Boolean state) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("accessPrivate",state);
    }

    public static Task<Void> setSubjectGroup(String groupName, String name) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("field",name);
    }

    public static Task<Void> setPublicationModerator(String groupName, Boolean onlyModerator) {
        return GroupHelper.getGroupCollection()
                .document(groupName)
                .update("publicationOnlyModerator",onlyModerator);
    }



    // --- DELETE ---
    public static Task<Void> deleteGroup(String groupName) {
        return GroupHelper.getGroupCollection().document(groupName).delete();
    }
}
