package com.example.socialmediaproject.ui;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.socialmediaproject.MainActivity;
import com.example.socialmediaproject.R;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    TextView forgotPassword;
    Button login;
    float v=0;

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
                Toast.makeText(getContext(),"Logique métier login pas encore implémentée...", Toast.LENGTH_LONG).show();

                // Redirection vers Main Activity depuis le bouton Login
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
