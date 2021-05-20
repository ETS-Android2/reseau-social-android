package com.example.socialmediaproject.ui.mes_reseaux;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.MainActivity;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;

import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.mes_reseaux.groupe.newPostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements PostAdapter.Listener {

    private PostAdapter postAdapter;
    @Nullable
    Group currentGroup;
    String groupName;
    private User user;
    private RecyclerView recyclerView;
    private ImageView img;

    // Lien image recup entre activité
    private Uri imageUri;
    private StorageReference refImg;

    // pour placer l'image
    private String urlImageToSend;
    private ImageView imageToSend;
    private CardView cardView_imageToSend;
    private Button button_send_message;

    // Couleur du bouton envoyer un message en fonction de si il est clickable ou non
    int colorEnabled ;
    int colorNotEnabled ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlImageToSend = "null";
        imageUri = Uri.EMPTY;

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img = findViewById(R.id.item_icon);

        colorEnabled = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        colorNotEnabled = ContextCompat.getColor(getApplicationContext(), R.color.colorLight);

        recyclerView = findViewById(R.id.recyclerView_group_chat);
        EditText editText_content = findViewById(R.id.editText_content_message);

        ImageButton button_load_image = findViewById(R.id.button_load_image);
        button_send_message = findViewById(R.id.send_message);

        cardView_imageToSend = findViewById(R.id.cardView_imageToSend);
        imageToSend = findViewById(R.id.imageToSend);

        // On n'affiche pas l'image à envoyé tant qu"il n'y a pas d'image select par le user
        cardView_imageToSend.setVisibility(View.GONE);
        imageToSend.setVisibility(View.GONE);


        // on récupère l'objet du fragment précédent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.groupName = bundle.getString("group_name");

            configureToolbar();
            configureRecyclerView(groupName);


            /**
             *
             * ON CLICK SUR LE BOUTON AJOUTER UNE IMAGE
             */
            button_load_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialisation variables pour l'image
                    imageUri = Uri.EMPTY;
                    urlImageToSend = "null";
                    choosePicture();
                }
            });

            /* picked picture */
            imageToSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    //builder.setTitle("Action");

                    String[] actions = {"Supprimer l'image"};
                    builder.setItems(actions, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                // Choisir une nouvelle image de profile
                                // ON RE INITIALISE LES VARIABLES POUR L'IMAGE
                                imageUri = Uri.EMPTY;
                                imageToSend.setImageResource(0);
                                urlImageToSend = "null";
                                cardView_imageToSend.setVisibility(View.GONE);
                                imageToSend.setVisibility(View.GONE);
                                // on repasse la couleur du bouton en non envoyé si le texte est vide
                                if(editText_content.getText().toString().length() !=0){
                                    button_send_message.setTextColor(colorEnabled);
                                }else{
                                    button_send_message.setTextColor(colorNotEnabled);
                                }
                                break;
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });




            // Lorsqu'il y a un changement sur l'edit text
            editText_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // si le text est vide
                    if(s.length() == 0){
                        button_send_message.setTextColor((!Uri.EMPTY.equals(imageUri)) ? colorEnabled : colorNotEnabled);
                    }else{
                        button_send_message.setTextColor(colorEnabled);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });


            /**
             *
             * APPUYER SUR LE BOUTON ENVOYER
             *
             */
            button_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String randomKey = UUID.randomUUID().toString();
                    refImg = BaseActivity.getRefStorage().child("images/" + randomKey);

                    if (!Uri.EMPTY.equals(imageUri)) {
                        // Msg with picture
                        refImg.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //BaseActivity.getRefUser().update("urlPicture", refImg.toString());
                                        urlImageToSend = refImg.toString();

                                        Post message = new Post(editText_content.getText().toString(), groupName, BaseActivity.getUid(), urlImageToSend);
                                        PostHelper.createPostForGroup(message).addOnFailureListener(onFailureListener());
                                        Toast.makeText(ChatActivity.this, "Image uploaded !", Toast.LENGTH_SHORT).show();

                                        // On reset
                                        editText_content.setText("");
                                        imageUri = Uri.EMPTY;
                                        urlImageToSend = "null";
                                        cardView_imageToSend.setVisibility(View.GONE);
                                        imageToSend.setVisibility(View.GONE);
                                        button_send_message.setTextColor(colorNotEnabled);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ChatActivity.this, "Failed to upload !", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else if (editText_content.getText().toString().length() != 0) {
                        // Msg without picture
                        Post message = new Post(editText_content.getText().toString(), groupName, BaseActivity.getUid(), "null");
                        PostHelper.createPostForGroup(message).addOnFailureListener(onFailureListener());

                        Toast.makeText(ChatActivity.this, "No picture !", Toast.LENGTH_SHORT).show();

                        // On reset
                        editText_content.setText("");
                        imageUri = Uri.EMPTY;
                        urlImageToSend = "null";
                        cardView_imageToSend.setVisibility(View.GONE);
                        imageToSend.setVisibility(View.GONE);
                        button_send_message.setTextColor(colorNotEnabled);
                    } else {
                        Toast.makeText(ChatActivity.this, "Ecrire du texte ou ajouter une image", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageToSend.setImageURI(imageUri);
            // on
            cardView_imageToSend.setVisibility(View.VISIBLE);
            imageToSend.setVisibility(View.VISIBLE);
            button_send_message.setTextColor(colorEnabled);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
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
                // On n'as pas encore mis en place la navigation vers les paramètres du chat

                // On ferme l'activité
                finish();

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