package com.example.socialmediaproject.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

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

        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Paramètres du groupe");
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initSettings();
        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Paramètres du groupe");
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        Bundle bundle = getArguments();
        groupName = bundle.getString("group_name");

        String key = preference.getKey();

        if(key.equals("group_invite")){
            Toast.makeText(getContext(),"Générer un code d'invitation !" , Toast.LENGTH_SHORT).show();
        }

        if(key.equals("group_edit")){
            Toast.makeText(getContext(),"Modifier le groupe !" , Toast.LENGTH_SHORT).show();
            bundle.putString("group_name", groupName);
            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsEditGroupFragment, bundle);
        }


        if(key.equals("group_waitlist")){
            Toast.makeText(getContext(),"Gérer les demandes d'adhésions !" , Toast.LENGTH_SHORT).show();
            bundle.putString("group_name", groupName);
            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_waitlistFragment, bundle);
        }
        if(key.equals("group_members")){
            Toast.makeText(getContext(),"Gérer les adhérents !" , Toast.LENGTH_SHORT).show();
            bundle.putString("group_name", groupName);
            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsGroupFragment_pageMembers, bundle);
        }

        if(key.equals("group_exit")){
            GroupHelper.removeUserFromGroup(currentGroup.getName(), BaseActivity.getUid())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_navigation_dashboard);
                            Toast.makeText(getContext(), "Quitter le groupe  !", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if(key.equals("group_delete")){

            GroupHelper.deleteGroup(currentGroup.getName())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_navigation_dashboard);
                            Toast.makeText(getContext(), "Quitter le groupe  !", Toast.LENGTH_SHORT).show();
                        }
                    });
            Toast.makeText(getContext(),"Supprimer le groupe !" , Toast.LENGTH_SHORT).show();
        }

        return super.onPreferenceTreeClick(preference);
    }


    private void initSettings(){
        Preference preferenceInvitation = findPreference("category_invitations");
        Preference preferenceExitGroup = findPreference("group_exit");
        Preference preferenceEditGroup = findPreference("group_edit");
        Preference preferenceEditWaitlistGroup = findPreference("group_waitlist");
        Preference preferenceEditMembersGroup = findPreference("group_members");
        Preference preferenceDeleteGroup = findPreference("group_delete");
/*
        preferenceInvitation.setVisible(false);
        preferenceExitGroup.setVisible(false);
        preferenceEditWaitlistGroup.setVisible(false);
        preferenceEditGroup.setVisible(false);
        preferenceEditMembersGroup.setVisible(false);
        preferenceDeleteGroup.setVisible(false);
*/

        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        groupName = bundle.getString("group_name");


        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentGroup = documentSnapshot.toObject(Group.class);



                // Si le compte connecté est l'admin du groupe
                if(currentGroup.getAdmin().equals(BaseActivity.getUid())){

                    // si on est en mode privé alors on affiche la catégorie d'invitation, sinon on n'affiche pas
                    preferenceInvitation.setVisible(currentGroup.getAccessPrivate());

                    preferenceExitGroup.setVisible(false);
                    preferenceEditWaitlistGroup.setVisible(true);
                    preferenceEditGroup.setVisible(true);
                    preferenceEditMembersGroup.setVisible(true);
                    preferenceDeleteGroup.setVisible(true);
                }else{
                    // Si le compte connecté est un membre
                    preferenceInvitation.setVisible(false);
                    preferenceExitGroup.setVisible(true);
                    preferenceEditWaitlistGroup.setVisible(false);
                    preferenceEditGroup.setVisible(false);
                    preferenceEditMembersGroup.setVisible(false);
                    preferenceDeleteGroup.setVisible(false);
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