package com.example.socialmediaproject.ui.mes_reseaux.groupe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.mails.JavaMailAPI;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class newPostActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @Nullable private User modelCurrentUser;
    private Uri imageUri;
    private ImageView imageView;
    StorageReference refImg;
    String urlImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        Bundle bundle = getIntent().getExtras();
        String groupeName =  bundle.getString("group_name");
        String groupType = bundle.getString("group_type");


        imageUri = Uri.EMPTY;
        urlImg = "null";

        this.getCurrentUserFromFirestore();

        imageView = findViewById(R.id.image_uploaded);
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

                    Log.d("=====> GROUP TYPE : ", groupType);

                    switch(groupType){
                        case "sms":
                            sendSms(editText_content.getText().toString(), groupeName, BaseActivity.getUid());
                            break;
                        case "email":
                            sendEmail(editText_content.getText().toString(), groupeName, BaseActivity.getUid());
                            break;
                        default:
                            sendPost(editText_content.getText().toString(), groupeName, BaseActivity.getUid());
                            break;
                    }

                    finish();
                }

            }
        });

        FloatingActionButton btn = findViewById(R.id.addPicture);
        imageView.setVisibility(View.GONE);

        /* add picture button */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                imageView.setVisibility(View.VISIBLE);
            }
        });

        /* picked picture */
        imageView.setOnClickListener(new View.OnClickListener() {
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
                            imageView.setImageResource(0);
                            urlImg = "null";
                            imageView.setVisibility(View.GONE);
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
            imageView.setImageURI(imageUri);
            Log.d("PICTURE ==> ", imageView.toString());
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
            urlImg = "null";
            Post post = new Post(content, group, userId, urlImg);
            PostHelper.createPostForGroup(post).addOnFailureListener(onFailureListener());

            Toast.makeText(newPostActivity.this, "No picture !", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSms(String content, String group, String userId){

        GroupHelper.getGroup(group).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                List<String> members = (List<String>) document.get("members");

                for(String idMember : members){
                    UserHelper.getUser(idMember).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            User user = new User();
                            user = task.getResult().toObject(User.class);

                            if(!user.getUid().equals(userId)){

                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(user.getPhoneNumber(), null, content, null, null);
                                    Toast.makeText(getApplicationContext(), "Message Sent",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {
                                    Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                                            Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                }

                sendPost(content, group, userId);
            }
        });
    }

    public void sendEmail(String content, String group, String userId){

        GroupHelper.getGroup(group).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                List<String> members = (List<String>) document.get("members");

                for(String idMember : members){
                    UserHelper.getUser(idMember).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            User user = new User();
                            user = task.getResult().toObject(User.class);

                            if(!user.getUid().equals(userId)){

                                try {
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(newPostActivity.this, user.getEmail(), "Envoyé depuis l'app Socializing, groupe : " + group, content);
                                    javaMailAPI.execute();
                                }
                                catch (Exception e){
                                    Log.d("EMAIL NOT SENT : ", e.getMessage());
                                }
                            }
                        }
                    });
                }
                sendPost(content, group, userId);
            }
        });
    }

    public void sendEmailVersion2(String content, String group, String userId){

        GroupHelper.getGroup(group).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                List<String> members = (List<String>) document.get("members");

                for(String idMember : members){
                    UserHelper.getUser(idMember).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            User user = new User();
                            user = task.getResult().toObject(User.class);

                            if(!user.getUid().equals(userId)){

                                Log.i("Send email", "");

                                String[] TO = {user.getEmail()};
                                String[] CC = {"plateformestage2021@gmail.com"};
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("text/plain");

                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, group);
                                emailIntent.putExtra(Intent.EXTRA_TEXT, content);

                                try {
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                    finish();
                                    Log.i("Email Status : ", "Finished sending email...");
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(newPostActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

                sendPost(content, group, userId);
            }
        });
    }
}