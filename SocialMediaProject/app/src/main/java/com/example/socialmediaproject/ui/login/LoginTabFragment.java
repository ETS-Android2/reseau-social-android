package com.example.socialmediaproject.ui.login;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    TextView forgotPassword;
    Button login;
    float v=0;

    Intent intent;
    FirebaseAuth fAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        fAuth = FirebaseAuth.getInstance();

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
                Toasty.info(getContext(), "Logique métier forgotPassword pas encore implémentée...", Toast.LENGTH_SHORT, true).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_value = email.getText().toString();
                final String password_value = password.getText().toString();

                if(validateFields(email_value, password_value))
                    authenticate(email_value, password_value);

            }
        });

        return root;
    }

    public boolean validateFields(String email, String password){

        boolean validate = true;

        if(email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){

            Toasty.warning(getContext(), "email incorrect", Toast.LENGTH_SHORT, true).show();
            validate = false;
        }
        else if(password.isEmpty() || password.length()<4){
            Toasty.warning(getContext(), "Entrer + de 4 caractères pour le mot de passe", Toast.LENGTH_SHORT, true).show();
            validate = false;
        }

        return validate;
    }

    public void authenticate(String email, String password){
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toasty.success(getContext(), "Vous êtes connecté !", Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toasty.error(getContext(), "Erreur :"+ " " + task.getException().getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            }
        });
    }

}
