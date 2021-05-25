package com.example.socialmediaproject.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.LoginAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton microsoft, google, apple;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        microsoft = findViewById(R.id.fab_microsoft);
        google = findViewById(R.id.fab_google);
        apple = findViewById(R.id.fab_apple);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.button_login));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.button_signup));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        microsoft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toasty.info(getApplicationContext(), "Connexion via microsoft pas encore dispo...", Toast.LENGTH_SHORT, false).show();
            }
        });

        google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toasty.info(getApplicationContext(), "Connexion via google pas encore dispo...", Toast.LENGTH_SHORT, false).show();
            }
        });

        apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(getApplicationContext(), "Connexion via apple pas encore dispo...", Toast.LENGTH_SHORT, false).show();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
                Log.i("TAG","onTabSelected"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("TAG", "onTabUnselected" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
                Log.i("TAG","onTabReselected"+tab.getPosition());
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        microsoft.setTranslationY(300);
        google.setTranslationY(300);
        apple.setTranslationY(300);

        tabLayout.setTranslationY(300);

        microsoft.setAlpha(v);
        google.setAlpha(v);
        apple.setAlpha(v);

        tabLayout.setAlpha(v);

        microsoft.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        apple.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }
}