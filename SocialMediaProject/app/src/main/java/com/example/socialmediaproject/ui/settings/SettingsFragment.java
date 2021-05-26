package com.example.socialmediaproject.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import es.dmoral.toasty.Toasty;

public class SettingsFragment extends PreferenceFragmentCompat {

    Intent intent;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SwitchPreference preferenceSwitchDarkMode = findPreference("theme_dark_mode");


        // Changer le groupe en privé ou public
        preferenceSwitchDarkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toasty.info(getContext(), newValue.equals(true) ? "Activé" : "Désactivé" , Toast.LENGTH_SHORT, false).show();
                AppCompatDelegate.setDefaultNightMode(newValue.equals(true) ?
                        AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO);
                return true;

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // affichage de la flèche retour en arrière dans le menu
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // title fragment in the header
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Paramètres");
    }

    @Override
    public void onResume() {
        super.onResume();
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Paramètres");
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if(key.equals("notifications")){
            Toasty.info(getContext(),"Afficher page settings notifications !" , Toast.LENGTH_SHORT, false).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingsFragment_to_settingsNotificationFragment);
        }

        if(key.equals("deconnexion")){
                Toasty.success(getContext(), "Déconnexion", Toast.LENGTH_SHORT, true).show();

            if(BaseActivity.isCurrentUserLogged())
                BaseActivity.getAuth().signOut();

            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
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