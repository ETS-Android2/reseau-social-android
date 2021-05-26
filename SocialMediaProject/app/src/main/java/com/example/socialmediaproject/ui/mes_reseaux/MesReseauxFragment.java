package com.example.socialmediaproject.ui.mes_reseaux;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.firestore.Query;

public class MesReseauxFragment extends Fragment implements GroupAdapter.Listener{

    private MesReseauxViewModel mesReseauxViewModel;

    private TextView textViewRecyclerViewEmpty;
    private RecyclerView recyclerView;

    private String typeGroupFragment;

    // FOR DATA
    // 2 - Declaring Adapter and data
    private GroupAdapter groupAdapter;
    private String currentGroupName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mesReseauxViewModel = new ViewModelProvider(this).get(MesReseauxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mes_reseaux, container, false);

        recyclerView = root.findViewById(R.id.recyclerView_groups);
        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);

        this.configureToolbar();

        this.typeGroupFragment = "all";
        configureRecyclerView(this.typeGroupFragment);

        // we get the selected tab
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_type_group);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: // all
                        typeGroupFragment = "all";
                        configureRecyclerView( typeGroupFragment);
                        break;
                    case 1: // posts
                        typeGroupFragment = "post";
                        configureRecyclerView( typeGroupFragment);
                        break;
                    case 2: // Tchat
                        typeGroupFragment = "chat";
                        configureRecyclerView( typeGroupFragment);
                        break;
                    case 3: // email
                        typeGroupFragment = "email";
                        configureRecyclerView( typeGroupFragment);
                        break;
                    case 4: // sms
                        typeGroupFragment = "sms";
                        configureRecyclerView( typeGroupFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

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
        inflater.inflate(R.menu.reseaux_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Mise en place de la logique métier de la search bar
        MenuItem menuItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(newText.toLowerCase());
                return false;
            }
        });
    }

    private void processSearch(String query) {

        // en fonction du fragment selectionner on recherche le groupe
        if(typeGroupFragment.equals("all")){
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(
                    GroupHelper.getAllGroup(BaseActivity.getUid())
                            .orderBy("search")
                            .startAt(query)
                            .endAt(query+"\uf8ff")
            ), Glide.with(this), this);
        }else{
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(
                    GroupHelper.getAllGroupByType(typeGroupFragment, BaseActivity.getUid())
                            .orderBy("search")
                            .startAt(query)
                            .endAt(query+"\uf8ff")), Glide.with(this), this);
        }

        groupAdapter.startListening();
        recyclerView.setAdapter(groupAdapter);
        // Message affiché lorsque aucun groupe n'est trouvé
        textViewRecyclerViewEmpty.setText(R.string.messageNoGroupFound);
        textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }




    public void configureToolbar(){
        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_networks);
    }



    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String type){

        //Configure Adapter & RecyclerView
        if(type.equals("all")){
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroup(BaseActivity.getUid())),
                    Glide.with(this), this);
        }else{
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroupByType(type, BaseActivity.getUid())),
                    Glide.with(this), this);
        }

        // Affichage des groupes dans un grid de deux colonnes
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(this.groupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty

        if(typeGroupFragment.equals("all")){
            textViewRecyclerViewEmpty.setText(R.string.messageNoGroup);
        }else{
            textViewRecyclerViewEmpty.setText(R.string.messageNoGroupType);
        }
        textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


}