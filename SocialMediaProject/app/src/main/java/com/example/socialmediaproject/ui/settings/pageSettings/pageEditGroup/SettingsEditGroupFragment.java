package com.example.socialmediaproject.ui.settings.pageSettings.pageEditGroup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
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

        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentGroup = documentSnapshot.toObject(Group.class);

                // si on est en mode post alors on affiche sinon on ne l'affiche pas
                preferencePublication.setVisible(currentGroup.getType().equals("post"));

                // initialisation des paramètre du groupe
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                prefs.putBoolean("group_edit_privacy", currentGroup.getAccessPrivate());
                prefs.putString("group_edit_name", currentGroup.getName());
                prefs.apply();

                Preference preferencePublication = findPreference("group_edit_publication");

                // Changer le groupe en privé ou public
                SwitchPreference preferenceSwitch = findPreference("group_edit_privacy");
                preferenceSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if(newValue.equals(true)){
                            GroupHelper.setGroupAccess(currentGroup.getName(),true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"private : true" , Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            GroupHelper.setGroupAccess(currentGroup.getName(),false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(),"private : false" , Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
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
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Modifier le groupe");
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