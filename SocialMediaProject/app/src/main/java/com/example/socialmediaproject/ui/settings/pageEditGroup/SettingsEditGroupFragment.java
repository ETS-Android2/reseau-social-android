package com.example.socialmediaproject.ui.settings.pageEditGroup;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.models.Group;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;


public class SettingsEditGroupFragment extends PreferenceFragmentCompat{

    Group currentGroup;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_group_preferences, rootKey);

        Bundle bundle = getArguments();
        String groupName = bundle.getString("group_name");

        Preference preferencePublication = findPreference("group_edit_publication");
        //EditTextPreference preferenceNameGroup = findPreference("group_edit_name");
        SwitchPreference preferenceSwitch = findPreference("group_edit_privacy");
        ListPreference preferenceSubjectGroup = findPreference("group_edit_subject");

        // initialisation tant qu'on a pas récupérer les données du groupe courant
        //preferenceNameGroup.setText("");

        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentGroup = documentSnapshot.toObject(Group.class);


                // si on est en mode post alors on affiche sinon on ne l'affiche pas
                preferencePublication.setVisible(currentGroup.getType().equals("post"));

                //preferenceNameGroup.setText(currentGroup.getName());
                preferenceSwitch.setChecked(currentGroup.getAccessPrivate());
                CharSequence[] entriesNames = {"Lieu","Thème","Centre d'intéret", "Service"};
                CharSequence[] entriesValues = {"lieu","theme","centre_interet", "service"};
                preferenceSubjectGroup.setEntryValues(entriesValues);
                preferenceSubjectGroup.setEntries(entriesNames);
                int i=0;
                for(CharSequence item : entriesValues){
                    if(item.toString().equals(currentGroup.getField())){
                        preferenceSubjectGroup.setValueIndex(i);
                    }
                    i++;
                }



                // Changer le groupe en privé ou public
                preferenceSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        GroupHelper.setGroupAccess(currentGroup.getName(),newValue.equals(true))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), newValue.equals(true) ? "private" : "public" , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        return true;

                    }
                });

                preferenceSubjectGroup.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        GroupHelper.setSubjectGroup(currentGroup.getName(),newValue.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(),"Changement effectué !" , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        return true;

                    }
                });

                preferencePublication.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        GroupHelper.setPublicationModerator(currentGroup.getName(),newValue.toString().equals("onlyModerator"))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(),"onlyModerator" , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        return true;

                    }
                });
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String groupName = bundle.getString("group_name");
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(groupName);
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