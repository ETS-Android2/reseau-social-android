package com.example.socialmediaproject.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupAdapter;
import com.example.socialmediaproject.adapters.GroupItemAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;

import com.example.socialmediaproject.enums.Access;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements GroupAdapter.Listener{

    private DashboardViewModel dashboardViewModel;
    private String m_Text = "";

    private RecyclerView recyclerView;

    // FOR DATA
    // 2 - Declaring Adapter and data
    private GroupAdapter groupAdapter;
    @Nullable private User modelCurrentUser;
    private String currentGroupName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recyclerView_groups);

        this.configureToolbar();
        this.getCurrentUserFromFirestore();

        this.configureRecyclerView("all");

        /* Ancienne version avec grid view
        // list of group
        List<Group> groupAllItemList = new ArrayList<>();
        groupAllItemList.add(new Group("Les étudiants de montpellier","post", "test",new User("antoine")));
        groupAllItemList.add(new Group("Les motards du 36","sms", "test",new User("steve")));
        groupAllItemList.add(new Group("Végan un jour, Végan toujours","email", "test",new User("steve")));
        groupAllItemList.add(new Group("FDS - informatique","tchat", "test",new User("antoine")));
        groupAllItemList.add(new Group("Les fans de Squeezie","post", "test",new User("antoine")));

        List<Group> groupPostItemList = new ArrayList<>();
        List<Group> groupTchatItemList = new ArrayList<>();
        List<Group> groupSMSItemList = new ArrayList<>();
        List<Group> groupEmailItemList = new ArrayList<>();

        for(Group item : groupAllItemList){
            switch (item.getType()){
                case "post": groupPostItemList.add(item); break;
                case "email": groupEmailItemList.add(item); break;
                case "sms": groupSMSItemList.add(item); break;
                case "tchat": groupTchatItemList.add(item); break;
            }

        }

        // get grid view
        GridView allPost = (GridView) root.findViewById(R.id.gridView_mes_reseaux);
        allPost.setAdapter(new GroupItemAdapter(getContext(), groupAllItemList));


*/

        // we get the selected tab
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_type_group);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: // all
                        configureRecyclerView( "all");
                        //allPost.setAdapter(new GroupItemAdapter(getContext(), groupAllItemList));
                        break;
                    case 1: // posts
                        configureRecyclerView( "post");
                        //allPost.setAdapter(new GroupItemAdapter(getContext(), groupPostItemList));
                        break;
                    case 2: // Tchat
                        configureRecyclerView( "chat");
                        //allPost.setAdapter(new GroupItemAdapter(getContext(), groupTchatItemList));
                        break;
                    case 3: // email
                        configureRecyclerView( "email");
                        //allPost.setAdapter(new GroupItemAdapter(getContext(), groupEmailItemList));
                        break;
                    case 4: // sms
                        configureRecyclerView( "sms");
                        //allPost.setAdapter(new GroupItemAdapter(getContext(), groupSMSItemList));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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


    public void configureToolbar(){
        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes réseaux");
    }


    // --------------------
    // REST REQUESTS
    // --------------------
    // 4 - Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        /*UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                modelCurrentUser = documentSnapshot.toObject(User.class);
            }
        });*/
    }

    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String type){

        //Configure Adapter & RecyclerView
        if(type.equals("all")){
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroup()),
                    Glide.with(this), this, "test user");
        }else{
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroupByType(type)),
                    Glide.with(this), this, "test user");
        }

        groupAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(groupAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.groupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        //textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    // --------------------
    // OTHERS
    // --------------------

    void openPrivateGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Entre le code d'accès à un groupe privé pour le rejoindre");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}