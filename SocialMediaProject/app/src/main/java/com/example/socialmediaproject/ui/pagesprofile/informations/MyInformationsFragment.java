package com.example.socialmediaproject.ui.pagesprofile.informations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.db.UserRoomDatabase;
import com.example.socialmediaproject.db.dao.UserDao;
import com.example.socialmediaproject.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyInformationsFragment extends Fragment {

    private MyInformationsViewModel mViewModel;
    private Button btn;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private UserRoomDatabase userDB;
    private UserDao userDao;
    private User user;

    public static MyInformationsFragment newInstance() {
        return new MyInformationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_informations_fragment, container, false);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes informations");

        btn = view.findViewById(R.id.button_update_informations);

        // on rempli les informations de l'utilisateur
        TextInputEditText textEdit_email = view.findViewById(R.id.editText_email);
        TextInputEditText textEdit_phone = view.findViewById(R.id.editText_phone);
        TextInputEditText textEdit_birth_date = view.findViewById(R.id.editText_birth_date);
        textEdit_email.setText(user.getEmail());
        textEdit_phone.setText(user.getPhoneNumber());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields(textEdit_email.getText().toString(), textEdit_phone.getText().toString()))
                    updateInformations(textEdit_email.getText().toString(), textEdit_phone.getText().toString());

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyInformationsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userDB = UserRoomDatabase.getDatabase(getActivity());
        userDao = userDB.userDao();
        user = userDao.getUser(fAuth.getCurrentUser().getUid()).getUser();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateFields(String email, String phone){

        boolean validate = true;

        if(email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(getContext(), "The email address must be correct", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if(phone.isEmpty() || !phone.matches("^0[6-7]{1}[0-9]{8}$")){
            Toast.makeText(getContext(), "The phone mustn't be empty and be correct", Toast.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }

    public void updateInformations(String email, String phone){
        DocumentReference ref = fStore.collection("users").document(fAuth.getUid());
        ref.update("email", email);
        ref.update("phoneNumber", phone);
    }

}