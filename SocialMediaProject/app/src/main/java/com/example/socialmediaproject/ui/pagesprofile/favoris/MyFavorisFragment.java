package com.example.socialmediaproject.ui.pagesprofile.favoris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class MyFavorisFragment extends Fragment {

    private RecyclerView recyclerView;

    private MyFavorisViewModel mViewModel;

    public static MyFavorisFragment newInstance() {
        return new MyFavorisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.my_favoris_fragment, container, false);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes favoris");

        // list of group
        List<Group> groupAllItemList = new ArrayList<>();
        groupAllItemList.add(new Group("Les étudiants de montpellier","post", "test",new User("antoine")));
        groupAllItemList.add(new Group("Les motards du 36","sms", "test",new User("antoine")));
        groupAllItemList.add(new Group("Végan un jour, Végan toujours","email", "test",new User("antoine")));
        groupAllItemList.add(new Group("FDS - informatique","tchat", "test",new User("antoine")));
        groupAllItemList.add(new Group("Les fans de Squeezie","post", "test",new User("antoine")));


        // list of posts
        List<Post> postItemList = new ArrayList<>();
        postItemList.add(new Post(groupAllItemList.get(1), new User("antoine")));
        postItemList.add(new Post(groupAllItemList.get(1), new User("thomas")));

        recyclerView = view.findViewById(R.id.recyclerView_myFavoris);

        PostAdapter myAdapter = new PostAdapter(getContext(), postItemList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyFavorisViewModel.class);
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


}