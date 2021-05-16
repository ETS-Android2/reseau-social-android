package com.example.socialmediaproject.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmediaproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity {

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
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            //Date past = format.parse("27/04/2021");
            Date past = myDate;
            Date now = new Date();
            if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 60){
                if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 1){
                    return "maintenant";
                }else{
                    return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " min";
                }

            }else{
                if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) < 24){
                    return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " h";
                }else{
                    if(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 1){
                        return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " jour";
                    }else{
                        return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " jours";
                    }

                }
            }

        }
        catch (Exception j){
            j.printStackTrace();
            return "error";
        }
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    public static FirebaseFirestore getStore() { return FirebaseFirestore.getInstance(); }
    @Nullable
    public static FirebaseAuth getAuth(){ return FirebaseAuth.getInstance(); }
    @Nullable
    public static FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    @Nullable
    public static String getUid(){ return getCurrentUser().getUid(); }

    public static Boolean isCurrentUserLogged(){ return (getCurrentUser() != null); }

    public static DocumentReference getRefUser() { return getStore().collection("users").document(getUid()); }
}
