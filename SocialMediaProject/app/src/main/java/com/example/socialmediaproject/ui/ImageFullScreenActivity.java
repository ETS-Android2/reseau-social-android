package com.example.socialmediaproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        LinearLayout myLinearLayout = findViewById(R.id.layout_image_full_screen);
        ImageView imageView = findViewById(R.id.image_in_full_screen);

        /**
         *
         * RÉCUPÉRATION DE L'IMAGE ET AFFICHAGE
         */
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String urlImg = bundle.getString("urlImage");

            if(!urlImg.equals("null")){
                Glide.with(this)
                        .load(BaseActivity.getRefImg(urlImg))
                        .into(imageView);
            }else{
                finish();
            }
        }


        /**
         *
         * ON FERME L'ACTIVITÉ QUAND ON CLIQUE SUR LE LAYOUT, DONC N'IMPORTE OU SUR LA PAGE
         */
        myLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}