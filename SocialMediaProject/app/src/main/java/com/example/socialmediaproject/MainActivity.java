package com.example.socialmediaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.socialmediaproject.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottomAppBar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_newGroup, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setSupportActionBar(findViewById(R.id.materialToolbar));
        NavigationUI.setupWithNavController(navView, navController);


        // action sur le bouton flottant du menu pour ajouter un groupe
        /*
        FloatingActionButton fab = findViewById(R.id.btnAddGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Nouveau groupe!" , Toast.LENGTH_SHORT).show();
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.newGroupFragment);
            }
        });*/
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(BaseActivity.isCurrentUserLogged()){
            Log.d("===========> ", "USER IS LOGGED !");
        }
        else{
            Log.d("===========> ", "USER IS NOT LOGGED !");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}