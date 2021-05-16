package com.example.socialmediaproject.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.ProfileItemAdapter;
import com.example.socialmediaproject.adapters.UserAdapter;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.ProfileItem;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView username;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) root.findViewById(R.id.username);

        UserHelper.getUser(BaseActivity.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                user = task.getResult().toObject(User.class);
                username.setText(user.getUsername());
            }
        });

        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // list of posts
        List<ProfileItem> profileItemList = new ArrayList<>();
        profileItemList.add(new ProfileItem("Créer un groupe","add_circle"));
        profileItemList.add(new ProfileItem("Mes posts","article"));
        profileItemList.add(new ProfileItem("Mes groupes (admin)","groups"));
        profileItemList.add(new ProfileItem("Mes informations","info"));

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
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_settingsFragment);
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }
}