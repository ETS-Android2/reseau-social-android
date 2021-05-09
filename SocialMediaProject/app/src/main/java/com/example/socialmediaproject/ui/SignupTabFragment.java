package com.example.socialmediaproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmediaproject.LoginActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.UserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupTabFragment extends Fragment {

    EditText email, name, phone, password;
    Button signup;
    float v=0;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        email = root.findViewById(R.id.email);
        name = root.findViewById(R.id.name);
        phone = root.findViewById(R.id.phone);
        password = root.findViewById(R.id.password);
        signup = root.findViewById(R.id.signup);

        email.setTranslationX(800);
        name.setTranslationX(800);
        phone.setTranslationX(800);
        password.setTranslationX(800);
        signup.setTranslationX(800);

        email.setAlpha(v);
        name.setAlpha(v);
        phone.setAlpha(v);
        password.setAlpha(v);
        signup.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_value = email.getText().toString();
                final String name_value = name.getText().toString();
                final String phone_value = phone.getText().toString();
                final String password_value = password.getText().toString();

                Query emailAddress = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email_value);
                Query phoneNumber = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("phoneNumber").equalTo(phone_value);

                emailAddress.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0) {
                            Toast.makeText(getContext(), "This Email Address is already using...", Toast.LENGTH_LONG).show();
                        }
                        else{
                            phoneNumber.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getChildrenCount() > 0) {
                                        Toast.makeText(getContext(), "This Phone Number is already using...", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if(validateFields(name_value, phone_value, email_value, password_value))
                                            registerUser(name_value, phone_value, email_value, password_value);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    throw error.toException();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });

            }
        });

        return root;
    }

    public void registerUser(String name, String phone, String email, String password){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        String key =  rootNode.getReference("users").push().getKey();

        UserHelper helperClass = new UserHelper(name, phone, email, password);
        reference.child(key).setValue(helperClass);

        Toast.makeText(getContext(), "User created !", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean validateFields(String name, String phone, String email, String password){

        boolean validate = true;

        if(name.isEmpty() || name.length()<3 || !name.matches("^([^0-9]*)$")){
            Toast.makeText(getContext(), "The name must be at least 3 characters long and not contain number", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if(phone.isEmpty() || !phone.matches("^0[6-7]{1}[0-9]{8}$")){
            Toast.makeText(getContext(), "The phone mustn't be empty and be correct", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if(email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(getContext(), "The email address must be correct", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if(password.isEmpty() || password.length()<4){
            Toast.makeText(getContext(), "Password must be at least 4 characters", Toast.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }
}
