package com.example.socialmediaproject.ui.home;

import android.content.DialogInterface;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private String m_Text = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // list of group
        List<Group> groupAllItemList = new ArrayList<>();
        //groupAllItemList.add(new Group("Les étudiants de montpellier","post", "test",new User("antoine")));
        //groupAllItemList.add(new Group("Les motards du 36","sms", "test",new User("antoine")));
        //groupAllItemList.add(new Group("Végan un jour, Végan toujours","email", "test",new User("antoine")));
        //groupAllItemList.add(new Group("FDS - informatique","tchat", "test",new User("antoine")));
        //groupAllItemList.add(new Group("Les fans de Squeezie","post", "test",new User("antoine")));


        // list of posts
        List<Post> postItemList = new ArrayList<>();
        //postItemList.add(new Post(groupAllItemList.get(1), new User("antoine")));
        //postItemList.add(new Post(groupAllItemList.get(1), new User("thomas")));
        //postItemList.add(new Post(groupAllItemList.get(1), new User("enzo")));
        //postItemList.add(new Post(groupAllItemList.get(1), new User("pedro")));
        //postItemList.add(new Post(groupAllItemList.get(1), new User("josé")));

        // get list view
        //ListView allPost = (ListView) root.findViewById(R.id.ListView_posts);
        //allPost.setAdapter(new PostItemAdapter(getContext(), postItemList));

        recyclerView = root.findViewById(R.id.recyclerView_home_posts);

        //PostAdapter myAdapter = new PostAdapter(getContext(), postItemList);
        //ecyclerView.setAdapter(myAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("App Projet Android");
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