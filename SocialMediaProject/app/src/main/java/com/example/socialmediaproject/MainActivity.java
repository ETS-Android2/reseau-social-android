package com.example.socialmediaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.db.UserRoomDatabase;
import com.example.socialmediaproject.db.dao.UserDao;
import com.example.socialmediaproject.db.entities.UserEntity;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private UserRoomDatabase userDB;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        userDB = UserRoomDatabase.getDatabase(getApplicationContext());
        userDao = userDB.userDao();

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
        if(fAuth.getCurrentUser() == null){
            Log.d("===========> ", "USER IS NOT LOGGED !");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else{
            Log.d("===========> ", "USER IS LOGGED !");

            UserHelper.getUser(fAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d("RESULT : ", "getResult: " + task.getResult().toObject(User.class));
                        UserEntity user = task.getResult().toObject(User.class).getEntity();
                        userDao.insertOnlySingleRecord(user);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "UserHelper.getUser error " + task.getException().getMessage(), Toast.LENGTH_LONG);
                    }
                }
            });
        }
    }

}