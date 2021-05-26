package com.example.socialmediaproject.base;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Notif;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity {
    public static List<Notif> notifs = new ArrayList<Notif>();
    public static boolean beNotified = true;
    // --------------------
    // LIFE CYCLE
    // --------------------

    /*
    *   On utilise pas (pour le moment)
    *
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getFragmentLayout());
        ButterKnife.bind(this); //Configure Butterknife
    }
    */


    /*
    *   On utilise pas (pour le moment)
    *
    public abstract int getFragmentLayout();
    */

    // --------------------
    // UI
    // --------------------

    /*
    *   On utilise pas (pour le moment)
    *
    protected void configureToolbar(){
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
    */

    public static String getTimeAgo(Date myDate){

        String text_now = "now"; //context.getResources().getString(R.string.text_now);
        String text_day = "d";//context.getResources().getString(R.string.text_day);
        String text_hour = "h"; //context.getResources().getString(R.string.text_hour);
        String text_min = "min"; //context.getResources().getString(R.string.text_min);

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            //Date past = format.parse("27/04/2021");
            Date past = myDate;
            Date now = new Date();
            if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 60){
                if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 1){
                    return text_now;
                }else{
                    return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " " + text_min;
                }

            }else{
                if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) < 24){
                    return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " " + text_hour;
                }else{
                    return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " " + text_day;
                }
            }

        }
        catch (Exception j){
            j.printStackTrace();
            return text_now;
        }
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erreur inconnu.", Toast.LENGTH_LONG).show();
            }
        };
    }

    // --------------------
    // UTILS
    // --------------------

    /* Firestore */
    @Nullable
    public static FirebaseFirestore getStore() { return FirebaseFirestore.getInstance(); }
    public static DocumentReference getRefUser() { return getStore().collection("users").document(getUid()); }

    /* Fireauth */
    @Nullable
    public static FirebaseAuth getAuth(){ return FirebaseAuth.getInstance(); }
    @Nullable
    public static FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    @Nullable
    public static String getUid(){ return getCurrentUser().getUid(); }
    public static Boolean isCurrentUserLogged(){ return (getCurrentUser() != null); }

    /* Storage */
    @Nullable
    public static FirebaseStorage getStorage(){ return FirebaseStorage.getInstance(); }
    public static  StorageReference getRefStorage() { return getStorage().getReference(); }
    public static StorageReference getRefImg(String url) { return getStorage().getReferenceFromUrl(url); }

    /* Messaging */
    public static FirebaseMessaging getMessaging(){ return FirebaseMessaging.getInstance(); }

    public static Task<String> getToken(){ return FirebaseMessaging.getInstance().getToken(); }


}
