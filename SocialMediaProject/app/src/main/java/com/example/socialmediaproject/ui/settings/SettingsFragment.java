package com.example.socialmediaproject.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaproject.MainActivity;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import es.dmoral.toasty.Toasty;

public class SettingsFragment extends PreferenceFragmentCompat {

    Intent intent;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        this.configToolBar();
        // On initialise les paramètres de l'application
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        settings.getBoolean("theme_dark_mode",false);

        SharedPreferences.Editor editor = settings.edit();
        settings.edit().apply();

        SwitchPreference preferenceSwitchDarkMode = findPreference("theme_dark_mode");

        //preferenceSwitchDarkMode.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        // Changer le groupe en privé ou public
        preferenceSwitchDarkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                AppCompatDelegate.setDefaultNightMode(newValue.equals(true) ?
                        AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO);

                // On sauvegarde les paramètres du dark mode
                editor.putBoolean("theme_dark_mode", AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES); // value to store
                editor.apply();
                return true;

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        this.configToolBar();
    }

    void configToolBar(){

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Paramètres");


        }catch(Exception e){
            Log.d("ERRROOOOOOR", e.getMessage());
            //Toasty.error(getContext(), "erroooooor" , Toast.LENGTH_SHORT, false).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // title fragment in the header
        this.configToolBar();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if(key.equals("notifications")){
            //Toasty.info(getContext(),"Afficher page settings notifications !" , Toast.LENGTH_SHORT, false).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingsFragment_to_settingsNotificationFragment);
        }

        if(key.equals("deconnexion")){
            //Toasty.success(getContext(), getContext().getResources().getString(R.string.message_logout), Toast.LENGTH_SHORT, true).show();

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