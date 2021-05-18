package com.example.socialmediaproject.ui.mes_reseaux.groupe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class newPostActivity extends AppCompatActivity {


    @Nullable private User modelCurrentUser;
    private Uri imageUri;
    private CircularImageView img;
    StorageReference refImg;
    String urlImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String groupeName =  bundle.getString("group_name");

        imageUri = Uri.EMPTY;
        urlImg = "null";

        this.getCurrentUserFromFirestore();

        img = findViewById(R.id.image_uploaded);
        EditText editText_content = findViewById(R.id.editTextTextMultiLine);


        ImageButton imageButton_close = findViewById(R.id.toolbar_close);
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermeture de l'activité
                //Toast.makeText(getApplicationContext(),"Fermer l'activité !" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button textView_create_post = findViewById(R.id.toolbar_post);
        textView_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_content.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Vous devez saisir du texte avant de poster votre message !" , Toast.LENGTH_SHORT).show();
                }else{
                    sendPost(editText_content.getText().toString(), groupeName, BaseActivity.getUid());
                    finish();
                }

            }
        });

        FloatingActionButton btn = findViewById(R.id.addPicture);
        CardView card = findViewById(R.id.card);

        /* add picture button */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.setVisibility(View.VISIBLE);
                choosePicture();
            }
        });

        /* picked picture */
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(newPostActivity.this);
                //builder.setTitle("Action");

                String[] actions = {"Supprimer l'image"};
                builder.setItems(actions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Choisir une nouvelle image de profile
                            imageUri = Uri.EMPTY;
                            img.setImageResource(0);
                            urlImg = "null";
                            card.setVisibility(View.GONE);
                            break;
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // 4 - Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                modelCurrentUser = documentSnapshot.toObject(User.class);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
            Log.d("PICTURE ==> ", img.toString());
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void sendPost(String content, String group, String userId){
        final String randomKey = UUID.randomUUID().toString();
        refImg = BaseActivity.getRefStorage().child("images/" + randomKey);

        if(!Uri.EMPTY.equals(imageUri)) {
            // Msg with picture
            refImg.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //BaseActivity.getRefUser().update("urlPicture", refImg.toString());
                            urlImg = refImg.toString();

                            Post post = new Post(content, group, userId, urlImg);
                            PostHelper.createPostForGroup(post).addOnFailureListener(onFailureListener());

                            Toast.makeText(newPostActivity.this, "Image uploaded !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(newPostActivity.this, "Failed to upload !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        }
                    });
        }
        else{
            // Msg without picture
            Post post = new Post(content, group, userId, urlImg);
            PostHelper.createPostForGroup(post).addOnFailureListener(onFailureListener());

            Toast.makeText(newPostActivity.this, "No picture !", Toast.LENGTH_SHORT).show();
        }
    }
}