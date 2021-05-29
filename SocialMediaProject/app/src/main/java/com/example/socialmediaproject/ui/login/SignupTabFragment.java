package com.example.socialmediaproject.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class SignupTabFragment extends Fragment {

    EditText email, name, phone, password;
    Button signup;
    float v=0;

    String userID;;

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

                if(validateFields(name_value, phone_value, email_value, password_value))
                    registerUser(name_value, phone_value, email_value, password_value);

            }
        });

        return root;
    }

    public void registerUser(String name, String phone, String email, String password){

        BaseActivity.getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toasty.success(getContext(), "Inscription réussie", Toast.LENGTH_SHORT, true).show();

                    DocumentReference documentReference = BaseActivity.getRefUser();

                    User user = new User(BaseActivity.getUid(), name, phone, email);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SIGNUP SUCCESS :", "onSuccess: user is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("SIGNUP ERROR :", "onFailure :" + e.toString());
                        }
                    });

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(getContext(), "error :" + " " + task.getException().getMessage(), Toasty.LENGTH_LONG, true).show();
                }

            }
        });
    }

    public boolean validateFields(String name, String phone, String email, String password){

        boolean validate = true;

        if(name.isEmpty() || name.length()<3 || !name.matches("^([^0-9]*)$")){
            Toasty.warning(getContext(), "The name must be at least 3 characters long and not contain number", Toasty.LENGTH_LONG, true).show();
            validate = false;
        }
        else if(phone.isEmpty() || !phone.matches("^0[6-7]{1}[0-9]{8}$")){
            Toasty.warning(getContext(), "The phone mustn't be empty and be correct", Toasty.LENGTH_LONG, true).show();
            validate = false;
        }
        else if(email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toasty.warning(getContext(), "The email address must be correct", Toasty.LENGTH_LONG, true).show();
            validate = false;
        }
        else if(password.isEmpty() || password.length()<4){
            Toasty.warning(getContext(), "Password must be at least 4 characters", Toasty.LENGTH_LONG, true).show();
            validate = false;
        }

        return validate;
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getContext(), getString(R.string.welcome_login), Toast.LENGTH_LONG).show();
            }
        };
    }
}
