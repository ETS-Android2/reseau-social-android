package com.example.socialmediaproject.ui;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmediaproject.LoginActivity;
import com.example.socialmediaproject.MainActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.db.UserRoomDatabase;
import com.example.socialmediaproject.db.dao.UserDao;
import com.example.socialmediaproject.db.entities.UserEntity;
import com.example.socialmediaproject.models.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    TextView forgotPassword;
    Button login;
    float v=0;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String userId;
    UserHelperClass user;
    Intent intent;

    UserRoomDatabase userDB;
    UserDao userDao;

    public LoginTabFragment(){
        userDB = UserRoomDatabase.getDatabase(getActivity());
        userDao = userDB.userDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forgotPassword = root.findViewById(R.id.forget_password);
        login = root.findViewById(R.id.login);

        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPassword.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Logique métier forgotPassword pas encore implémentée...", Toast.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_value = email.getText().toString();
                final String password_value = password.getText().toString();

                if(validateFields(email_value, password_value))
                    isUser(email_value, password_value);

            }
        });

        return root;
    }

    public boolean validateFields(String email, String password){

        boolean validate = true;

        if(email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(getContext(), "The email address must be correct", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if(password.isEmpty() || password.length()<4){
            Toast.makeText(getContext(), "Password must be at least 4 characters", Toast.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }

    public void isUser(String email, String password){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        Query checkUser = reference.orderByChild("email").equalTo(email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userId = "";

                for(DataSnapshot child : snapshot.getChildren()){
                    userId = child.getKey();
                }

                if(snapshot.exists()){
                    Log.d("HERE :", snapshot.getValue().toString());
                    String passwordFromDB = snapshot.child(userId).child("password").getValue(String.class);

                    if(passwordFromDB != null && passwordFromDB.equals(password)){
                        String emailFromDB = snapshot.child(userId).child("email").getValue(String.class);
                        String nameFromDB = snapshot.child(userId).child("name").getValue(String.class);
                        String phoneNumberFromDB = snapshot.child(userId).child("phoneNumber").getValue(String.class);

                        intent = new Intent(getActivity(), MainActivity.class);

                        user = new UserHelperClass(nameFromDB, phoneNumberFromDB, emailFromDB, passwordFromDB);
                        userDao.deleteAll();
                        userDao.insert(new UserEntity(userId, user.getEmail(), user.getName(), user.getPassword(), user.getPhoneNumber()));

                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "No such User exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}
