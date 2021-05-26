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

import com.example.socialmediaproject.adapters.PostAdapterForHome;
import com.example.socialmediaproject.adapters.UserAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private String m_Text = "";

    // FOR DATA
    // 2 - Declaring Adapter and data
    private PostAdapterForHome postAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);

        recyclerView = root.findViewById(R.id.recyclerView_home_posts);

        // Add a divider between posts
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        this.configureToolbar();
        this.configureRecyclerView();

        swipeRefreshLayout.setRefreshing(true);
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
        /*this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPost()),
                    Glide.with(this), this);

         */




        // On récupère tous les groupes post où l'utilisateur courrant à adhéré
        GroupHelper.getAllGroupByType("post", BaseActivity.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Group> listGroupOfUser = queryDocumentSnapshots.toObjects(Group.class);
                        Log.d("SIZE_LISTE_GROUPS",listGroupOfUser.size()+" groups");

                        // On place les noms des groupes récupérer dans une liste
                        ArrayList<String> listNomGroupsUser =  new ArrayList<String>();
                        for(Group item : listGroupOfUser){
                            listNomGroupsUser.add(item.getName());
                        }
                        // On récupère tous les posts
                        PostHelper.getAllPost().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                ArrayList<Post> listResultats = new ArrayList<Post>();
                                List<Post> listPosts = queryDocumentSnapshots.toObjects(Post.class);
                                for(Post post : listPosts){
                                    if(listNomGroupsUser.contains(post.getGroup())){
                                        listResultats.add(post);
                                    }
                                }

                                postAdapter = new PostAdapterForHome(listResultats);
                                recyclerView.setAdapter(postAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                });


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



}