package com.example.socialmediaproject.ui.settings.pageSettings.pageWaitlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.UserAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.settings.pageSettings.pageMembers.MembersListViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class WaitlistFragment extends Fragment {

    Group currentGroup;
    private WaitlistViewModel mViewModel;

    public static WaitlistFragment newInstance() {
        return new WaitlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waitlist, container, false);


        Bundle bundle = getArguments();
        String groupName = bundle.getString("group_name");

        // get list view
        ListView allUser = view.findViewById(R.id.listView_waitlist);


        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentGroup = documentSnapshot.toObject(Group.class);
                ArrayList<User> userWaitList = new ArrayList<>();
                for(String id : currentGroup.getWaitlist()){

                    UserHelper.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userWaitList.add(documentSnapshot.toObject(User.class));
                            allUser.setAdapter(new UserAdapter(getContext(), userWaitList, currentGroup, true));
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Demandes adhésions");

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