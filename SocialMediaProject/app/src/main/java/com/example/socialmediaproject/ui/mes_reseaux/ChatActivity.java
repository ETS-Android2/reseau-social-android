package com.example.socialmediaproject.ui.mes_reseaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.ui.mes_reseaux.groupe.ChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page_activity);
        setSupportActionBar(findViewById(R.id.materialToolbar));
        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();

            bundle.putString("group_name",bundle.getString("group_name"));
            bundle.putString("group_type", "chat");
            ChatFragment fragment = new ChatFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow();
        }
    }
}