package com.example.socialmediaproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaproject.R;

public class newPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButton_close = findViewById(R.id.toolbar_close);
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermeture de l'activité
                //Toast.makeText(getApplicationContext(),"Fermer l'activité !" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button textView_create_post = (Button) findViewById(R.id.toolbar_post);
        textView_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Poster sur le groupe !" , Toast.LENGTH_SHORT).show();

                // temporaire
                finish();
            }
        });
    }
}