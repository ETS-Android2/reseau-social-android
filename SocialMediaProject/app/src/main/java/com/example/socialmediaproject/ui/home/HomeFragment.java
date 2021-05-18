package com.example.socialmediaproject.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.adapters.PostAdapter;

import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class HomeFragment extends Fragment implements PostAdapter.Listener {

    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private String m_Text = "";

    // FOR DATA
    // 2 - Declaring Adapter and data
    private PostAdapter postAdapter;

    private TextView textViewRecyclerViewEmpty;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView itemIcon;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        progressBar = root.findViewById(R.id.idProgressBar);
        recyclerView = root.findViewById(R.id.recyclerView_home_posts);

        // Add a divider between posts
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //itemIcon = recyclerView.findViewById(R.id.item_icon);

        this.configureToolbar();
        this.configureRecyclerView();

        // Sets up the swipe refrash
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // On re execute la requête et on remet en place le recycler view avec les nouvelles données
                // une fois que le chargement esy terminé, on setRefreshing(false)
                configureRecyclerView();
            }
        });


        return root;
    }


    public void configureToolbar(){
        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(){
        //Configure Adapter & RecyclerView //PostHelper.getAllPost(BaseActivity.getUid())
        this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPost()),
                    Glide.with(this), this);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(this.postAdapter);
        // On met sur false la progresse bar qui indique le raffrachissment
        swipeRefreshLayout.setRefreshing(false);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty


        // La progresse bar est afficher quand il n'y a pas d'article
        progressBar.setVisibility(this.postAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        textViewRecyclerViewEmpty.setVisibility(this.postAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

}