package com.example.socialmediaproject.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.socialmediaproject.LoginActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.db.UserRoomDatabase;
import com.example.socialmediaproject.db.dao.UserDao;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Intent intent;
    private FirebaseAuth fAuth;
    private UserRoomDatabase userDB;
    private UserDao userDao;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        userDB = UserRoomDatabase.getDatabase(getActivity());
        userDao = userDB.userDao();

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Paramètres");
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
            Toast.makeText(getContext(),"Afficher page settings notifications !" , Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingsFragment_to_settingsNotificationFragment);
        }

        if(key.equals("deconnexion")){

            if(fAuth.getCurrentUser() != null)
                fAuth.signOut();

            userDao.deleteAll();
            Toast.makeText(getContext(), "Déconnexion", Toast.LENGTH_SHORT).show();
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