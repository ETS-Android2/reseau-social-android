package com.example.socialmediaproject.ui.mes_reseaux;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class ChatActivity extends AppCompatActivity implements PostAdapter.Listener {

    private PostAdapter postAdapter;
    @Nullable
    Group currentGroup;
    String groupName;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        EditText editText_content = findViewById(R.id.editText_content_message);


        // on récupère l'objet du fragment précédent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.groupName = bundle.getString("group_name");
            configureToolbar();

            recyclerView = findViewById(R.id.recyclerView_group_chat);
            configureRecyclerView(groupName);

            Button textView_send_message = findViewById(R.id.send_message);

            // On ne peut pas appuyer sur le bouton tant que le message est vide
            boolean contentIsEmpty =  editText_content.getText().toString().matches("");
            //textView_send_message
            //textView_send_message.setEnabled(!contentIsEmpty);

            textView_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText_content.getText().toString().matches("")){
                        Toast.makeText(getApplicationContext(),"Vous devez saisir du texte avant de poster votre message !" , Toast.LENGTH_SHORT).show();
                    }else{
                        Post message = new Post(editText_content.getText().toString(), groupName, BaseActivity.getUid());
                        PostHelper.createPostForGroup(message).addOnFailureListener(onFailureListener());
                        // On reset
                        editText_content.setText("");
                    }

                }
            });

        }
    }





    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erreur lors de l'envoie du message", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void configureToolbar(){
        // affichage de la flèche retour en arrière dans le menu
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // title fragment in the header bar
        this.getSupportActionBar().setTitle(groupName);
    }



    private void configureRecyclerView(String groupName){
        //Configure Adapter & RecyclerView
        this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getMessageFromGroup(groupName)),
                Glide.with(this), this, true, true);


        postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(postAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.postAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }


    // --------------------
    // OTHERS
    // --------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière
                this.onBackPressed();
                break;
            case R.id.group_menu_settings:
                Bundle bundle = new Bundle();
                bundle.putString("group_name", currentGroup.getName());

                //NavHostFragment.findNavController(this).navigate(R.id.action_navigation_groupe_post_to_settingsGroupFragment, bundle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------------
    // CALLBACK
    // --------------------


    @Override
    public void onDataChanged() {

    }

}