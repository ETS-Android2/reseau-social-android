package com.example.socialmediaproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.models.Post;
import com.google.android.gms.tasks.OnFailureListener;

public class newPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String groupeName =  bundle.getString("group_name");


        EditText editText_content = findViewById(R.id.editTextTextMultiLine);


        ImageButton imageButton_close = findViewById(R.id.toolbar_close);
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermeture de l'activité
                Toast.makeText(getApplicationContext(),"Fermer l'activité !" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button textView_create_post = findViewById(R.id.toolbar_post);
        textView_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_content.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Vous devez saisir du texte pour poster !" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Poster sur le groupe !" , Toast.LENGTH_SHORT).show();

                    Post post = new Post(editText_content.getText().toString(), groupeName,"MNGLupdbc0RfgZfysQGwDzyzE9h2");
                    PostHelper.createPostForGroup(post).addOnFailureListener(onFailureListener());
                    finish();
                }

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
}