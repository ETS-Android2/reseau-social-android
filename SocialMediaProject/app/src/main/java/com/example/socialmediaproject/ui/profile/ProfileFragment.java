package com.example.socialmediaproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.SettingsActivity;
import com.example.socialmediaproject.adapters.ProfileItemAdapter;
import com.example.socialmediaproject.models.ProfileItem;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        // list of posts
        List<ProfileItem> profileItemList = new ArrayList<>();
        profileItemList.add(new ProfileItem("Mes favoris"));
        profileItemList.add(new ProfileItem("Mes posts"));
        profileItemList.add(new ProfileItem("Mes groupes"));
        profileItemList.add(new ProfileItem("Mes informations"));

        // get list view
        ListView allPost = (ListView) root.findViewById(R.id.profileListView);
        allPost.setAdapter(new ProfileItemAdapter(getContext(), profileItemList));

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.profile_menu_settings:
                // ouverture de l'activité des paramètres de l'application
                openSettingsActivity();
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }

    void openSettingsActivity(){
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }
}