package com.example.socialmediaproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(BaseActivity.isCurrentUserLogged()){
            Log.d("===========> ", "USER IS LOGGED !");
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.bottomAppBar);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_mes_reseaux, R.id.navigation_newGroup, R.id.navigation_notifications, R.id.navigation_profile)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            setSupportActionBar(findViewById(R.id.materialToolbar));
            NavigationUI.setupWithNavController(navView, navController);
        }

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