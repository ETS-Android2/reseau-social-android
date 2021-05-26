package com.example.socialmediaproject.ui.mes_reseaux.groupe;


import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;

import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class PostGroupeFragment extends Fragment implements PostAdapter.Listener {


    private PostAdapter postAdapter;

    private Group currentGroup;
    private String groupName;
    private String groupType;
    private RecyclerView recyclerView;
    private PostGroupeViewModel mViewModel;

    private TextView textViewRecyclerViewEmpty;

    public static PostGroupeFragment newInstance() {
        return new PostGroupeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_groupe, container, false);

        CoordinatorLayout layout_group = root.findViewById(R.id.layout_group);
        layout_group.setVisibility(View.GONE);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);

        ImageView imageAccess = root.findViewById(R.id.group_acces_image);
        TextView tv_groupTitle = root.findViewById(R.id.group_title);
        TextView tv_groupType = root.findViewById(R.id.group_type);
        TextView tv_groupAccess = root.findViewById(R.id.group_acces);
        TextView tv_groupNbMembers = root.findViewById(R.id.group_members);

        // action sur le bouton flottant pour ajouter un post
        FloatingActionButton fab = root.findViewById(R.id.fab);
        // on cache le bouton et on l'aiichera une fois les donnees du groupe récupérer si on a l'autorisation de post
        fab.setVisibility(View.GONE);



        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        if(bundle != null){
            this.groupName = bundle.getString("group_name");
            this.groupType = bundle.getString("group_type");
            configureToolbar();

            GroupHelper.getGroupRef(groupName).addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("ERROOOOOOR", "Listen failed.", error);
                                return;
                            }

                            if(documentSnapshot != null && documentSnapshot.exists()){
                                currentGroup = documentSnapshot.toObject(Group.class);

                                layout_group.setVisibility(View.VISIBLE);

                                tv_groupTitle.setText(currentGroup.getName());
                                tv_groupType.setText(currentGroup.getType().toUpperCase());

                                // On enlève l'admin du compteur de member
                                String nbMembers;
                                if(currentGroup.getMembers().size() <= 1){
                                    nbMembers = currentGroup.getMembers().size() + " " + "membre"; // à l'infinitif
                                }else{
                                    nbMembers = currentGroup.getMembers().size() + " " + "membres"; // au pluriels
                                }
                                tv_groupNbMembers.setText(nbMembers);

                                if(currentGroup.getAccessPrivate()){

                                    tv_groupAccess.setText("Privé");
                                    imageAccess.setImageResource(R.drawable.ic_baseline_lock_24);
                                }else{
                                    tv_groupAccess.setText("Public");
                                    imageAccess.setImageResource(R.drawable.ic_baseline_lock_open_24);
                                }

                                // on cache le bouton si publication onlyModerator et qu'on est membre
                                boolean currentUserisModerator = currentGroup.getModerators().contains(BaseActivity.getUid());
                                boolean publicationOnlyModerator = currentGroup.getPublicationOnlyModerator();
                                if(!publicationOnlyModerator){
                                    // tous le monde peut post si c'est pas reservé au moderateurs
                                    fab.setVisibility(View.VISIBLE);
                                }else{
                                    // seul les modérateurs peuvent post
                                    fab.setVisibility(currentUserisModerator ? View.VISIBLE : View.GONE);
                                }



                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Toast.makeText(getContext(),"Ajouter un post dans ce groupe!" , Toast.LENGTH_SHORT).show();

                                        //UserHelper.addUserInGroup().addOnFailureListener(onFailureListener());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("group_name", currentGroup.getName());
                                        bundle.putString("group_type", currentGroup.getType());
                                        Intent intent = new Intent(getActivity(), newPostActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });


            recyclerView = root.findViewById(R.id.recyclerView_group_posts);
            if(groupType == null){
                configureRecyclerView(groupName, "other");
            }else{
                configureRecyclerView(groupName, groupType);
            }


        }
        return root;
    }


    public void configureToolbar(){
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(groupName);
    }



    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    // Seulement appeler quand les données du groupe sont chargé
    private void configureRecyclerView(String groupName, String groupType){
        //Configure Adapter & RecyclerView
        if(groupType.equals("chat")){
            this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPostForGroup(groupName)),
                    Glide.with(this), this, true, true);
        }else{
            this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPostForGroup(groupName)),
                    Glide.with(this), this, true);

        }



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.postAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }


    // --------------------
    // OTHERS
    // --------------------

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
            case R.id.group_menu_settings:
                Bundle bundle = new Bundle();
                if(!currentGroup.getType().equals("chat")){
                    bundle.putString("group_name", currentGroup.getName());
                    NavHostFragment.findNavController(this).navigate(R.id.action_navigation_groupe_to_settingsGroupFragment, bundle);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------------
    // CALLBACK
    // --------------------


    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.postAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

}