package com.example.socialmediaproject.ui.profile.mes_informations;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class MesInformationsFragment extends Fragment {

    private MesInformationsViewModel mViewModel;
    private Button btn;
    private User user;

    public static MesInformationsFragment newInstance() {
        return new MesInformationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mes_informations_fragment, container, false);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_informations);

        btn = view.findViewById(R.id.button_update_informations);

        // on rempli les informations de l'utilisateur
        TextInputEditText textEdit_email = view.findViewById(R.id.editText_email);
        TextInputEditText textEdit_phone = view.findViewById(R.id.editText_phone);

        UserHelper.getUser(BaseActivity.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                user = task.getResult().toObject(User.class);
                textEdit_email.setText(user.getEmail());
                textEdit_phone.setText(user.getPhoneNumber());
            }
        });

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
        mViewModel = new ViewModelProvider(this).get(MesInformationsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.profile_page_menu, menu);
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

            Toasty.error(getContext(), "Email incorrect", Toast.LENGTH_LONG, true).show();
            validate = false;
        }
        else if(phone.isEmpty() || !phone.matches("^0[6-7]{1}[0-9]{8}$")){

            Toasty.error(getContext(), "Phone incorrect", Toast.LENGTH_LONG, true).show();
            validate = false;
        }

        return validate;
    }

    public void updateInformations(String email, String phone){
        BaseActivity.getCurrentUser().updateEmail(email);
        BaseActivity.getRefUser().update("email", email);
        BaseActivity.getRefUser().update("phoneNumber", phone);

        Toasty.success(getContext(), "Mettre à jour", Toast.LENGTH_LONG, true).show();
    }

}