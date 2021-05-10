package com.example.socialmediaproject.ui.newGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Notif;
import com.example.socialmediaproject.models.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class NewGroupFragment extends Fragment {

    private AutoCompleteTextView spinnerGroupType;
    private AutoCompleteTextView spinnerGroupAccess;
    private AutoCompleteTextView spinnerGroupPublication;
    private AutoCompleteTextView spinnerGroupSubject;

    private FirebaseFirestore fStore;

    private NewGroupViewModel mViewModel;

    public static NewGroupFragment newInstance() {
        return new NewGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_group_fragment, container, false);


        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Nouveau Groupe");
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        EditText editText_groupName = view.findViewById(R.id.editText_group_name);

        spinnerGroupType = (AutoCompleteTextView)view.findViewById(R.id.spinner_group_type);
        spinnerGroupAccess = (AutoCompleteTextView)view.findViewById(R.id.spinner_group_access);
        spinnerGroupSubject = (AutoCompleteTextView)view.findViewById(R.id.spinner_group_subject);
        spinnerGroupPublication = (AutoCompleteTextView)view.findViewById(R.id.spinner_group_publication);
        ArrayAdapter<CharSequence> adapterTypeGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_type, android.R.layout.simple_spinner_item);
        adapterTypeGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterAccessGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_access, android.R.layout.simple_spinner_item);
        adapterAccessGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterPublicationGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_publication, android.R.layout.simple_spinner_item);
        adapterPublicationGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterSubjectGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_subject, android.R.layout.simple_spinner_item);
        adapterSubjectGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerGroupType.setAdapter(adapterTypeGroup);
        spinnerGroupAccess.setAdapter(adapterAccessGroup);
        spinnerGroupPublication.setAdapter(adapterPublicationGroup);
        spinnerGroupSubject.setAdapter(adapterSubjectGroup);



        Button createGroup = view.findViewById(R.id.button_create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                /*
                bundle.putString("groupName", editText_groupName.getText().toString());
                bundle.putString("groupType", spinnerGroupType.getText().toString());
                bundle.putString("groupAccess", spinnerGroupAccess.getText().toString());
                bundle.putString("groupPublication", spinnerGroupPublication.getText().toString());
                bundle.putString("groupSubject", spinnerGroupSubject.getText().toString());
                 */


                Group currentGroup = new Group("Chess Club", "SMS", "Chess", new User("John"));
                bundle.putSerializable("group", currentGroup);


                if(editText_groupName.getText().toString().matches("") ||
                        spinnerGroupType.getText().toString().matches("") ||
                        spinnerGroupAccess.getText().toString().matches("") ||
                        spinnerGroupPublication.getText().toString().matches("") ||
                        spinnerGroupSubject.getText().toString().matches("")){
                    Toast.makeText(getContext(),"Vous devez remplir tous les champs demandé !" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Groupe créé !" , Toast.LENGTH_SHORT).show();

                    GroupHelper.createGroup("test","test","test",new User("test"))
                            .addOnFailureListener(onFailureListener());

                    Navigation.findNavController(view).navigate(R.id.action_newGroupFragment_to_navigation_groupe_post, bundle);
                }

            }
        });

        return view;
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewGroupViewModel.class);
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
        inflater.inflate(R.menu.close_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.close_menu_exit: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}