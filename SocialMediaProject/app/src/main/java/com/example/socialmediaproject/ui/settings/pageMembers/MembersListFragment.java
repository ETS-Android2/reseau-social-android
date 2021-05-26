package com.example.socialmediaproject.ui.settings.pageMembers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.socialmediaproject.ui.settings.SettingsGroupFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class MembersListFragment extends Fragment {

    Group currentGroup;

    private MembersListViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView allUser;

    public static MembersListFragment newInstance() {
        return new MembersListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.member_list);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_members_list, container, false);

        Bundle bundle = getArguments();
        String groupName = bundle.getString("group_name");

        // get list view
        allUser = view.findViewById(R.id.listView_members);
        this.configureListView();

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_memberlist);
        swipeRefreshLayout.setRefreshing(true);
        // Sets up the swipe refrash
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // On re execute la requête et on remet en place le recycler view avec les nouvelles données
                // une fois que le chargement esy terminé, on setRefreshing(false)
                configureListView();
            }
        });



            return view;
    }

    private void configureListView(){
        Bundle bundle = getArguments();
        String groupName = bundle.getString("group_name");

        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentGroup = documentSnapshot.toObject(Group.class);
                ArrayList<User> userList = new ArrayList<>();

                for(String id : currentGroup.getMembers()){

                    UserHelper.getUser(id).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userList.add(documentSnapshot.toObject(User.class));

                            // on set l'adapter
                            allUser.setAdapter(new UserAdapter(getContext(), userList, currentGroup));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MembersListViewModel.class);

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
                if(currentGroup.getType().equals("chat")){
                    Bundle bundle = getArguments();
                    bundle.putString("group_name",bundle.getString("group_name"));
                    SettingsGroupFragment fragment = new SettingsGroupFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow();
                }else{
                    getActivity().onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}