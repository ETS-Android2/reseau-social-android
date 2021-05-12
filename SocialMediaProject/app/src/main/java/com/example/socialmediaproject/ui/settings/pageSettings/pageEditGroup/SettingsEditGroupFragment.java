package com.example.socialmediaproject.ui.settings.pageSettings.pageEditGroup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Group;

public class SettingsEditGroupFragment extends PreferenceFragmentCompat {

    Group currentGroup;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_group_preferences, rootKey);

        currentGroup = new Group("test","test","test","test");

        Preference preferencePublication = findPreference("group_edit_publication");

        // si on est en mode post alors on affiche sinon on ne l'affiche pas
        preferencePublication.setVisible(currentGroup.getType().equals("post"));

        // initialisation des paramètre du groupe
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs.putBoolean("group_edit_privacy", currentGroup.isPrivate());
        prefs.putString("group_edit_name", currentGroup.getName());
        prefs.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        currentGroup = (Group) bundle.getSerializable("group");

        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs.putBoolean("group_edit_privacy", currentGroup.isPrivate());
        prefs.putString("group_edit_name", currentGroup.getName());
        prefs.apply();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        currentGroup = (Group) bundle.getSerializable("group");
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