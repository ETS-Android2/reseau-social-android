package com.example.socialmediaproject.ui.groupes.post;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.PostItemAdapter;
import com.example.socialmediaproject.models.PostItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostGroupeFragment extends Fragment {

    private PostGroupeViewModel mViewModel;

    public static PostGroupeFragment newInstance() {
        return new PostGroupeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_groupe_post, container, false);

        // list of posts
        List<PostItem> postItemList = new ArrayList<>();
        postItemList.add(new PostItem("Les étudiants de montpellier"));
        postItemList.add(new PostItem("Les motards du 36"));
        postItemList.add(new PostItem("Végan un jour, Végan toujours"));
        postItemList.add(new PostItem("FDS - informatique"));
        postItemList.add(new PostItem("Les fans de Squeezie"));

        // get list view
        ListView allPost = (ListView) root.findViewById(R.id.ListView_posts);
        allPost.setAdapter(new PostItemAdapter(getContext(), postItemList));


        // action sur le bouton flottant pour ajouter un post
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Ajouter un post dans ce groupe!" , Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PostGroupeViewModel.class);
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
        inflater.inflate(R.menu.group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}