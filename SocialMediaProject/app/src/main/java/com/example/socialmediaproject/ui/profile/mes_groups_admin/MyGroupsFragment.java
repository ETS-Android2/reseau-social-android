package com.example.socialmediaproject.ui.profile.mes_groups_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupAdapter;
import com.example.socialmediaproject.adapters.GroupGestionAdapter;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.adapters.SearchGroupAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class MyGroupsFragment extends Fragment implements GroupGestionAdapter.Listener {

    private GroupGestionAdapter groupAdapter;
    private TextView textViewRecyclerViewEmpty;
    private RecyclerView recyclerView;

    private MyGroupsViewModel mViewModel;

    public static MyGroupsFragment newInstance() {
        return new MyGroupsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_groups_fragment, container, false);

        this.configureToolbar();

        textViewRecyclerViewEmpty = view.findViewById(R.id.textViewRecyclerViewEmpty);
        recyclerView = view.findViewById(R.id.recycler_view_mes_groupes_admin);
        this.configureRecyclerView();
        return view;
    }

    public void configureToolbar(){
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_groups);
    }

    private void configureRecyclerView(){
        //Configure Adapter & RecyclerView
        this.groupAdapter = new GroupGestionAdapter(generateOptionsForAdapter(GroupHelper.getAllMyGroupAdmin(BaseActivity.getUid())),
                Glide.with(this), this);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.groupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyGroupsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.profile_page_menu, menu);
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


    @Override
    public void onDataChanged() {
        textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}