package com.example.socialmediaproject.ui.settings;

import android.os.Bundle;
import com.example.socialmediaproject.R;

import androidx.preference.PreferenceFragmentCompat;


public class SettingsNotificationFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notifications_preferences, rootKey);
    }
}