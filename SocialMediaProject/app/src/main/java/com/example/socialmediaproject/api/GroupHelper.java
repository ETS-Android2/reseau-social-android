package com.example.socialmediaproject.api;

import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    // --- CREATE ---
    public static Task<DocumentReference> createGroup(String name, String type, String field, User admin, Access access) {
        // 1 - Create User object
        Group groupToCreate = new Group(name, type, field, admin, access);
        // 2 - Add a new User Document to Firestore
        return GroupHelper.getGroupCollection()
                .document(name)
                .collection(COLLECTION_NAME)
                .add(groupToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getGroup(String id){
        return GroupHelper.getGroupCollection().document(id).get();
    }

    // --- UPDATE ---


    // --- DELETE ---
    public static Task<Void> deleteGroup(String id) {
        return GroupHelper.getGroupCollection().document(id).delete();
    }
}
