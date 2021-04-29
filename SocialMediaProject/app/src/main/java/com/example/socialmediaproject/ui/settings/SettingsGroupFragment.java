package com.example.socialmediaproject.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.enums.Publication;
import com.example.socialmediaproject.models.GroupItem;

public class SettingsGroupFragment extends PreferenceFragmentCompat {

    GroupItem currentGroup;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.groupe_preferences, rootKey);

        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        currentGroup = (GroupItem) bundle.getSerializable("group");



        Preference preferenceInvitation = findPreference("category_invitations");
        Preference preferenceExitGroup = findPreference("group_exit");
        Preference preferenceEditGroup = findPreference("group_edit");
        Preference preferenceEditMembersGroup = findPreference("group_members");
        Preference preferenceDeleteGroup = findPreference("group_delete");

        // Si le compte connecté est un membre
        if(true){
            preferenceInvitation.setVisible(false);
            preferenceExitGroup.setVisible(true);

            preferenceEditGroup.setVisible(false);
            preferenceEditMembersGroup.setVisible(false);
            preferenceDeleteGroup.setVisible(false);
        }else{ // Si le compte connecté est l'admin

            // si on est en mode privé alors on affiche la catégorie d'invitation, sinon on n'affiche pas
            preferenceInvitation.setVisible(currentGroup.isPrivate());

            preferenceExitGroup.setVisible(false);

            preferenceEditGroup.setVisible(true);
            preferenceEditMembersGroup.setVisible(true);
            preferenceDeleteGroup.setVisible(true);
        }


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        currentGroup = (GroupItem) bundle.getSerializable("group");

        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentGroup.getName());

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", currentGroup);

        if(key.equals("group_invite")){
            Toast.makeText(getContext(),"Générer un code d'invitation !" , Toast.LENGTH_SHORT).show();
        }

        if(key.equals("group_edit")){
            Toast.makeText(getContext(),"Modifier le groupe !" , Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsEditGroupFragment, bundle);
        }
        if(key.equals("group_members")){
            Toast.makeText(getContext(),"Gérer les adhérents !" , Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingsGroupFragment_to_settingsGroupFragment_pageMembers, bundle);
        }

        if(key.equals("group_exit")){
            Toast.makeText(getContext(),"Quitter le groupe !" , Toast.LENGTH_SHORT).show();
        }

        if(key.equals("group_delete")){
            Toast.makeText(getContext(),"Supprimer le groupe !" , Toast.LENGTH_SHORT).show();
        }

        return super.onPreferenceTreeClick(preference);
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
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}