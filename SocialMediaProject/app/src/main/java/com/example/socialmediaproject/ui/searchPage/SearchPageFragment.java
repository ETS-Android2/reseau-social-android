package com.example.socialmediaproject.ui.searchPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupAdapter;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.adapters.SearchGroupAdapter;
import com.example.socialmediaproject.api.CodeAccessHelper;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.CodeAccess;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.home.HomeViewModel;
import com.example.socialmediaproject.ui.mes_reseaux.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class SearchPageFragment extends Fragment implements SearchGroupAdapter.Listener {

    private RecyclerView recyclerView;
    private SearchPageViewModel mViewModel;
    private String m_Text = "";

    // FOR DATA
    // 2 - Declaring Adapter and data
    private SearchGroupAdapter searchGroupAdapter;

    // type de groupe selectionne dans le tab
    private String typeGroupFragment;

    private TextView textViewRecyclerViewEmpty;

    public static SearchPageFragment newInstance() {
        return new SearchPageFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchPageViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(SearchPageViewModel.class);
        View root = inflater.inflate(R.layout.search_page_fragment, container, false);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);

        this.configureToolbar();


        recyclerView = root.findViewById(R.id.recyclerView_search_group);

        this.typeGroupFragment = "all";
        configureRecyclerView(this.typeGroupFragment);

        // we get the selected tab
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_type_group);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: // all
                        typeGroupFragment = "all";
                        configureRecyclerView(typeGroupFragment);
                        break;
                    case 1: // posts
                        typeGroupFragment = "post";
                        configureRecyclerView(typeGroupFragment);
                        break;
                    case 2: // Tchat
                        typeGroupFragment = "chat";
                        configureRecyclerView(typeGroupFragment);
                        break;
                    case 3: // email
                        typeGroupFragment = "email";
                        configureRecyclerView(typeGroupFragment);
                        break;
                    case 4: // sms
                        typeGroupFragment = "sms";
                        configureRecyclerView(typeGroupFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        return root;
    }

    public void configureToolbar(){
        // on enlève l'affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.groups);
    }


    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String type){
        //Configure Adapter & RecyclerView
        if(type.equals("all")){
            this.searchGroupAdapter = new SearchGroupAdapter(generateOptionsForAdapter(GroupHelper.getAllPublicGroup()),
                    Glide.with(this), this);
        }else{
            this.searchGroupAdapter = new SearchGroupAdapter(generateOptionsForAdapter(GroupHelper.getAllPublicGroupByType(type)),
                    Glide.with(this), this);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.searchGroupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.search_group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Mise en place de la logique métier de la search bar
        MenuItem menuItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(newText.toLowerCase());
                return false;
            }
        });
    }

    private void processSearch(String query) {

        // en fonction du fragment selectionner on recherche le groupe
        if(typeGroupFragment.equals("all")){
            this.searchGroupAdapter = new SearchGroupAdapter(generateOptionsForAdapter(
                    GroupHelper.getAllPublicGroup()
                            .orderBy("search")
                            .startAt(query)
                            .endAt(query+"\uf8ff")
            ), Glide.with(this), this);
        }else{
            this.searchGroupAdapter = new SearchGroupAdapter(generateOptionsForAdapter(
                    GroupHelper.getAllPublicGroupByType(typeGroupFragment)
                            .orderBy("search")
                            .startAt(query)
                            .endAt(query+"\uf8ff")), Glide.with(this), this);
        }
        searchGroupAdapter.startListening();
        recyclerView.setAdapter(searchGroupAdapter);
        textViewRecyclerViewEmpty.setText("Aucun groupe trouvé.");
        textViewRecyclerViewEmpty.setVisibility(this.searchGroupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_menu_add_private_group:
                // ouverture de l'activité des paramètres de l'application
                openPrivateGroup();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    void openPrivateGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.access_code);

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);

        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputTextCode = input.getText().toString();
                if(inputTextCode.length() >= 1){

                    CodeAccessHelper.getCode(inputTextCode).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String groupName = documentSnapshot.toObject(CodeAccess.class).getGroupName();
                                // Le code est correct, on ajoute l'utilisateur dans la liste des membres
                                GroupHelper.addUserInGroup(groupName, BaseActivity.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        GroupHelper.getGroup(groupName).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                                Toasty.success(getContext(), "Code valide -> Accès au groupe : "+ " " + groupName, Toast.LENGTH_SHORT, true).show();
                                                CodeAccessHelper.deleteCode(inputTextCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // suppression du code car l'utilisation d'un code est unique.
                                                    }
                                                });

                                                if(documentSnapshot.toObject(Group.class).getType().equals("chat")){
                                                    // Si on est dans un groupe de type CHAT alors on ouvre le chat
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("group_name", groupName);
                                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                                    intent.putExtras(bundle);
                                                    getContext().startActivity(intent);
                                                }else{
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("group_name", groupName);
                                                    Navigation.findNavController(getView()).navigate(R.id.action_searchPageFragment_to_navigation_groupe, bundle);
                                                }






                                            }
                                        });
                                    };
                                });
                            } else {

                                Toasty.error(getContext(), "Code invalide !" , Toast.LENGTH_LONG, true).show();
                            }
                        }
                    });

                }else{

                    Toasty.warning(getContext(), "Code vide !" , Toast.LENGTH_LONG, true).show();
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        if(typeGroupFragment.equals("all")){
            textViewRecyclerViewEmpty.setText("Il n'y a pas de groupe public.");
        }else{
            textViewRecyclerViewEmpty.setText("Il n'y a pas de groupe public de ce type.");
        }

        textViewRecyclerViewEmpty.setVisibility(this.searchGroupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}