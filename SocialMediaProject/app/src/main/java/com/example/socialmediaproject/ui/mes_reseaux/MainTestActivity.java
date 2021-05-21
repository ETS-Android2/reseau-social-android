package com.example.socialmediaproject.ui.mes_reseaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.ui.mes_reseaux.ui.main.ChatFragment;

public class MainTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("group_name","lidl");
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