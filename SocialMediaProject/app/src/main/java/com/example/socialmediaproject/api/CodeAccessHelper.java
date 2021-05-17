package com.example.socialmediaproject.api;

import com.example.socialmediaproject.models.CodeAccess;
import com.example.socialmediaproject.models.Group;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/16/21.
 */
public class CodeAccessHelper {

    private static final String COLLECTION_NAME = "code_access";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getCodeAccessCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- GENERATION DU CODE D'ACCES ---
    /* L'identifiant du document est le code d'acces à un groupe privé (pratique car l'id du doc est unique dans la collection) */
    public static Task<DocumentReference> generateCode(CodeAccess codeAccess) {
        return CodeAccessHelper.getCodeAccessCollection().add(codeAccess);
    }

    public static Task<DocumentSnapshot> getCode(String code) {
        return CodeAccessHelper.getCodeAccessCollection().document(code).get();
    }

    // --- DELETE ---
    public static Task<Void> deleteCode(String code) {
        return CodeAccessHelper.getCodeAccessCollection().document(code).delete();
    }



}
