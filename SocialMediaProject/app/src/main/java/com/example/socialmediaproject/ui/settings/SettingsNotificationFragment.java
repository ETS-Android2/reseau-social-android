package com.example.socialmediaproject.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import es.dmoral.toasty.Toasty;


public class SettingsNotificationFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notifications_preferences, rootKey);

        SwitchPreferenceCompat preferenceSwitch = findPreference("notif_disabled");

        preferenceSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if(newValue.equals(true)) {
                    BaseActivity.beNotified = false;
                    //Toasty.success(getContext(), "Les notifications sont désactivés", Toast.LENGTH_SHORT, false).show();
                }
                else {
                    BaseActivity.beNotified = true;
                    //Toasty.success(getContext(), "Les notifications sont activés", Toast.LENGTH_SHORT, false).show();
                }

                return true;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);



        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // title fragment in the header

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");
    }

    @Override
    public void onResume() {
        super.onResume();
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");
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