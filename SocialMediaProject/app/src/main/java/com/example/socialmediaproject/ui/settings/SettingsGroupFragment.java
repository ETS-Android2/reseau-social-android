package com.example.socialmediaproject.ui.settings;

import android.content.ClipData;
import android.content.ClipboardManager;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.CodeAccessHelper;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.CodeAccess;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.ui.settings.pageEditGroup.SettingsEditGroupFragment;
import com.example.socialmediaproject.ui.settings.pageMembers.MembersListFragment;
import com.example.socialmediaproject.ui.settings.pageWaitlist.WaitlistFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.EventListener;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static androidx.core.content.ContextCompat.getSystemService;

public class SettingsGroupFragment extends PreferenceFragmentCompat {

    Group currentGroup;

    String groupName;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.groupe_preferences, rootKey);
        this.initSettings();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        groupName = bundle.getString("group_name");

        configToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        groupName = bundle.getString("group_name");

        configToolbar();

    }
    private void configToolbar(){
        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(groupName);
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        Bundle bundle = getArguments();
        groupName = bundle.getString("group_name");

        String key = preference.getKey();

        if(key.equals("group_invite")){

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // On créer un code d'accès pour le groupe
            CodeAccess newCode = new CodeAccess(groupName);
            CodeAccessHelper.generateCode(newCode).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {


                    builder.setTitle("Code : " + documentReference.getId());


                    String[] actions = { "Copier le code" };
                    builder.setItems(actions, (dialog, which) -> {
                        if (which == 0) {

                            Toasty.info(getContext(), "Le code est copié dans le presse-papier." , Toast.LENGTH_LONG, false).show();
                            // On copie dans le presse papier le code généré
                            ClipboardManager clipboard = getSystemService(requireContext(), ClipboardManager.class);
                            ClipData clip = ClipData.newPlainText("invitation", documentReference.getId());
                            assert clipboard != null;
                            clipboard.setPrimaryClip(clip);
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


        }

        if(key.equals("group_edit")){
            //Toast.makeText(getContext(),"Modifier le groupe !" , Toast.LENGTH_SHORT).show();
            bundle.putString("group_name", groupName);
            if(!currentGroup.getType().equals("chat")){
                Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsEditGroupFragment, bundle);

            }else{
                SettingsEditGroupFragment fragment = new SettingsEditGroupFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();
            }
        }


        if(key.equals("group_waitlist")){

            if(currentGroup.getWaitlist().size() == 0){

                //Toasty.info(getContext(),getString(R.string.message_nobody_in_waiting_list) , Toast.LENGTH_SHORT, false).show();
            }else{
                if(!currentGroup.getType().equals("chat")){
                    bundle.putString("group_name", groupName);
                    Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_waitlistFragment, bundle);
                }else{
                    WaitlistFragment fragment = new WaitlistFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow();
                }
            }

        }
        if(key.equals("group_members")){
            //Toast.makeText(getContext(),"Gérer les adhérents !" , Toast.LENGTH_SHORT).show();
            if(!currentGroup.getType().equals("chat")){

                bundle.putString("group_name", groupName);
                Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsGroupFragment_pageMembers, bundle);
            }else{
                MembersListFragment fragment = new MembersListFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();
            }

        }

        if(key.equals("group_exit")){
            GroupHelper.removeUserFromGroup(currentGroup.getName(), BaseActivity.getUid())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(!currentGroup.getType().equals("chat")) {
                                Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_navigation_mes_reseaux);
                            }else{
                               getActivity().finish();
                            }

                            //Toasty.info(getContext(), getString(R.string.message_exit_group), Toast.LENGTH_SHORT, false).show();
                        }
                    });
        }

        if(key.equals("group_delete")){
            /**
             *
             * SUPRESSION DU GROUPE
             *
             */
            GroupHelper.deleteGroup(currentGroup.getName())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            /**
                             *
                             * SUPRESSION DE TOUS LES POSTS ÉCRIT DANS LE GROUPE
                             *
                             */
                            PostHelper.getAllPostForGroup(groupName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot item : queryDocumentSnapshots){
                                        Log.d("POST A SUPP", item.getId());
                                        PostHelper.deletePost(item.getId()).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("ERRROR", "error lors de la suppression du post :"+item.getId());
                                            }
                                        });
                                    }
                                }
                            });













                            if(!currentGroup.getType().equals("chat")) {
                                Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_navigation_mes_reseaux);
                            }else{
                                getActivity().finish();
                            }
                        }
                    });
        }

        return super.onPreferenceTreeClick(preference);
    }


    private void initSettings(){
        Preference preferenceInvitation = findPreference("category_invitations");
        Preference preferenceGeneral = findPreference("category_general");
        Preference preferenceCategorieNotifications = findPreference("category_notifications");


        Preference preferenceExitGroup = findPreference("group_exit");
        Preference preferenceEditGroup = findPreference("group_edit");
        Preference preferenceEditWaitlistGroup = findPreference("group_waitlist");
        Preference preferenceEditMembersGroup = findPreference("group_members");
        Preference preferenceDeleteGroup = findPreference("group_delete");


        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        assert bundle != null;
        groupName = bundle.getString("group_name");


        // On écoute le document pour afficher les nouveaux changement
        GroupHelper.getGroupRef(groupName).addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w("Error listener : ", "Listen failed.", error);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    currentGroup = documentSnapshot.toObject(Group.class);


                    // On affiche tout une fois le groue chargé
                    preferenceGeneral.setVisible(true);
                    preferenceCategorieNotifications.setVisible(false);

                    if(currentGroup.getWaitlist().size() == 0){

                        preferenceEditWaitlistGroup.setSummary("Aucune demande");
                    }else{
                        assert preferenceEditWaitlistGroup != null;

                        preferenceEditWaitlistGroup.setSummary(currentGroup.getWaitlist().size() + " "+ "demande" +
                                (currentGroup.getWaitlist().size() == 1 ? " " : "s" ));
                    }

                    assert preferenceEditMembersGroup != null;
                    preferenceEditMembersGroup.setSummary(currentGroup.getMembers().size() + " "+ "membre" +
                            (currentGroup.getMembers().size() <= 1 ? " " : "s"));

                    // Si le compte connecté est l'admin du groupe
                    if(currentGroup.getAdmin().equals(BaseActivity.getUid())){

                        // si on est en mode privé alors on affiche la catégorie d'invitation, sinon on n'affiche pas
                        assert preferenceInvitation != null;
                        preferenceInvitation.setVisible(currentGroup.getAccessPrivate());
                        preferenceEditWaitlistGroup.setVisible(true);
                        preferenceEditGroup.setVisible(true);
                        preferenceEditMembersGroup.setVisible(true);
                        preferenceDeleteGroup.setVisible(true);

                        preferenceExitGroup.setVisible(false);
                    }else if(currentGroup.getModerators().contains(BaseActivity.getUid())){
                        // Si le compte connecté est un modérateur du groupe
                        preferenceInvitation.setVisible(currentGroup.getAccessPrivate());
                        preferenceEditWaitlistGroup.setVisible(true);
                        preferenceEditMembersGroup.setVisible(true);
                        preferenceExitGroup.setVisible(true);

                        preferenceEditGroup.setVisible(false);
                        preferenceDeleteGroup.setVisible(false);
                    } else{
                        // Si le compte connecté est un membre
                        preferenceEditMembersGroup.setVisible(true);
                        preferenceExitGroup.setVisible(true);

                        preferenceInvitation.setVisible(false);
                        preferenceEditWaitlistGroup.setVisible(false);
                        preferenceEditGroup.setVisible(false);
                        preferenceDeleteGroup.setVisible(false);
                    }

                } else {
                    Log.d("Error listener :" , "Current data: null");
                }
            }
        });


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière

                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(groupName);
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}