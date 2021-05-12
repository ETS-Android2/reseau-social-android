package com.example.socialmediaproject.ui.home;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.adapters.PostAdapter;

import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment implements PostAdapter.Listener {

    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private String m_Text = "";

    // FOR DATA
    // 2 - Declaring Adapter and data
    private PostAdapter postAdapter;
    @Nullable private User modelCurrentUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = root.findViewById(R.id.recyclerView_home_posts);

        this.configureToolbar();
        this.configureRecyclerView();

        return root;
    }


    public void configureToolbar(){
        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("App Projet Android");
    }

    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(){
        //Configure Adapter & RecyclerView
        this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPost()),
                    Glide.with(this), this, "test user");


        postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(postAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.postAdapter);
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
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_menu_add_private_group:
                // ouverture de l'activité des paramètres de l'application
                openPrivateGroup();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        //textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    void openPrivateGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Entre le code d'accès à un groupe privé pour le rejoindre");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}