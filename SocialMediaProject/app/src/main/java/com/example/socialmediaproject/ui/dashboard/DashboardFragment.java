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
import android.widget.GridView;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupItemAdapter;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.google.android.material.tabs.TabLayout;

import com.example.socialmediaproject.enums.Access;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String m_Text = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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

        // we get the selected tab
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_type_group);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: // all
                        allPost.setAdapter(new GroupItemAdapter(getContext(), groupAllItemList));
                        break;
                    case 1: // posts
                        allPost.setAdapter(new GroupItemAdapter(getContext(), groupPostItemList));
                        break;
                    case 2: // Tchat
                        allPost.setAdapter(new GroupItemAdapter(getContext(), groupTchatItemList));
                        break;
                    case 3: // email
                        allPost.setAdapter(new GroupItemAdapter(getContext(), groupEmailItemList));
                        break;
                    case 4: // sms
                        allPost.setAdapter(new GroupItemAdapter(getContext(), groupSMSItemList));
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


        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes réseaux");
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