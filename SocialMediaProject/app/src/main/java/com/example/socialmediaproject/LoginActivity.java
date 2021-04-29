package com.example.socialmediaproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaproject.adapters.LoginAdapter;
import com.example.socialmediaproject.db.UserRoomDatabase;
import com.example.socialmediaproject.db.dao.UserDao;
import com.example.socialmediaproject.db.entities.UserEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton microsoft, google, apple;
    Intent intent;
    float v=0;

    UserRoomDatabase userDB;
    UserDao userDao;
    List<UserEntity> listUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDB = UserRoomDatabase.getDatabase(getApplicationContext());
        userDao = userDB.userDao();
        listUser = userDao.getAll();

        Log.d("LOGIN :", String.valueOf(listUser.size()));

        if(listUser.size() > 0) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


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
                Toast.makeText(getApplicationContext(),"Connexion via microsoft pas encore dispo...", Toast.LENGTH_LONG).show();
            }
        });

        google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Connexion via google pas encore dispo...", Toast.LENGTH_LONG).show();
            }
        });

        apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Connexion via apple pas encore dispo...", Toast.LENGTH_LONG).show();
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