package com.example.socialmediaproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Notif;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(BaseActivity.isCurrentUserLogged()){

            /**
             *
             * On regarde les paramètres de l'application pour choisir le mode sombre ou le mode classique
             */
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean modeSombre = settings.getBoolean("theme_dark_mode",false);
            AppCompatDelegate.setDefaultNightMode(modeSombre ?
                        AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO);



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

            notification();
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

    protected void notification(){
        GroupHelper.getAllGroup(BaseActivity.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {


                if(error != null){
                    Log.w("Listen failed : ", "onEvent error", error);
                    return;
                }

                for(DocumentSnapshot group : value.getDocuments()){

                    List<String> membres = (List<String>) group.get("members");

                    if(membres.contains(BaseActivity.getUid())){
                        PostHelper.getAllPostForGroup(group.get("name").toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                if(error != null){
                                    Log.w("EXCEPTION", "Listen failed.", error);
                                    return;
                                }

                                for(DocumentChange dc : value.getDocumentChanges()){

                                    if(dc.getDocument().getData().get("dateCreated") == null)
                                        return;

                                    Date currentDate = new Date();
                                    Date dateCreated = ((Timestamp)dc.getDocument().getData().get("dateCreated")).toDate();

                                    long diff = TimeUnit.MILLISECONDS.toMinutes(currentDate.getTime() - dateCreated.getTime());
                                    long diff2 = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime() - dateCreated.getTime());

                                    Log.d("DIFF : ", String.valueOf(diff));

                                    if(!dc.getDocument().getData().get("userSender").toString().equals(BaseActivity.getUid()) && diff2 < 1 && BaseActivity.beNotified) {

                                        switch (dc.getType()) {
                                            case ADDED:
                                                UserHelper.getUser(dc.getDocument().getData().get("userSender").toString()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                        User user = task.getResult().toObject(User.class);
                                                        String content = dc.getDocument().getData().get("content").toString();
                                                        String groupName = group.get("name").toString();

                                                        // Notification
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

                                                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                            manager.createNotificationChannel(channel);

                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "n")
                                                                    .setContentTitle("Nouveau message envoyé par" +" " + user.getUsername())
                                                                    .setSmallIcon(R.drawable.twitter)
                                                                    .setAutoCancel(true)
                                                                    .setContentText(content);

                                                            Notif notif = new Notif("Nouveau message envoyé par " + user.getUsername(), content);

                                                            BaseActivity.notifs.add(notif);

                                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                                                            managerCompat.notify(999, builder.build());
                                                        }
                                                    }
                                                });
                                                break;
                                            case MODIFIED:
                                                break;
                                            case REMOVED:
                                                break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}