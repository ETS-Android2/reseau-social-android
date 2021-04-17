package com.example.socialmediaproject.ui;

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

import com.example.socialmediaproject.R;

public class SignupTabFragment extends Fragment {

    EditText email, name, phone, password;
    Button signup;
    float v=0;

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
                Toast.makeText(getContext(),"Logique métier signup pas encore implémentée...", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
