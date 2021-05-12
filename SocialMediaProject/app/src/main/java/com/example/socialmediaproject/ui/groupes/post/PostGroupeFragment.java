package com.example.socialmediaproject.ui.groupes.post;


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
import com.example.socialmediaproject.api.UserHelper;

import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.newPostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Map;


public class PostGroupeFragment extends Fragment implements PostAdapter.Listener {

    // FOR DESIGN
    // 1 - Getting all views needed


    // FOR DATA
    // 2 - Declaring Adapter and data
    private PostAdapter postAdapter;
    @Nullable private User modelCurrentUser;
    private String currentGroupName;

    Group currentGroup;
    private RecyclerView recyclerView;
    private PostGroupeViewModel mViewModel;

    private TextView textViewRecyclerViewEmpty;

    public static PostGroupeFragment newInstance() {
        return new PostGroupeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_groupe_post, container, false);

        CoordinatorLayout layout_group = root.findViewById(R.id.layout_group);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);

        ImageView imageAccess = root.findViewById(R.id.group_acces_image);
        TextView tv_groupTitle = root.findViewById(R.id.group_title);
        TextView tv_groupType = root.findViewById(R.id.group_type);
        TextView tv_groupAccess = root.findViewById(R.id.group_acces);
        TextView tv_groupNbMembers = root.findViewById(R.id.group_members);

        layout_group.setVisibility(View.GONE);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        if(bundle != null){
            GroupHelper.getGroup(bundle.getString("group_name"))
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Map<String, Object> dataGroup = documentSnapshot.getData();
                                currentGroup = new Group((String) dataGroup.get("name"), (String) dataGroup.get("type"),(String) dataGroup.get("field"), (String) dataGroup.get("admin"));
                                //Toast.makeText(getContext(),"Le groupe exist"+  dataGroup.get("type") , Toast.LENGTH_SHORT).show();

                                //currentGroup = new Group(documentSnapshot.get("name"), "type","field", "admin");

                                layout_group.setVisibility(View.VISIBLE);
                                tv_groupTitle.setText(currentGroup.getName());
                                tv_groupType.setText(currentGroup.getType());
                                tv_groupNbMembers.setText("50 members");

                                if(currentGroup.isPrivate()){
                                    tv_groupAccess.setText("private");
                                    imageAccess.setImageResource(R.drawable.ic_baseline_lock_24);
                                }else{
                                    tv_groupAccess.setText("public");
                                    imageAccess.setImageResource(R.drawable.ic_baseline_lock_open_24);
                                }


                                recyclerView = root.findViewById(R.id.recyclerView_group_posts);
                                configureRecyclerView(currentGroup.getName());
                                configureToolbar();


                                // action sur le bouton flottant pour ajouter un post
                                FloatingActionButton fab = root.findViewById(R.id.fab);
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getContext(),"Ajouter un post dans ce groupe!" , Toast.LENGTH_SHORT).show();

                                        //UserHelper.addUserInGroup().addOnFailureListener(onFailureListener());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("group_name", currentGroup.getName());
                                        Intent intent = new Intent(getActivity(), newPostActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });


                            }else{
                                Toast.makeText(getContext(),"Le groupe n'existe pas" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return root;
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    public void configureToolbar(){
        // title fragment in the header bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentGroup.getName());
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // 4 - Get Current User from Firestore



    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String groupName){
        //Track current group name
        this.currentGroupName = groupName;
        //Configure Adapter & RecyclerView
        this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getAllPostForGroup(this.currentGroupName)),
                Glide.with(this), this, "idUser");
        postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(postAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
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
    // CALLBACK
    // --------------------


    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.postAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
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
                bundle.putString("group_name", currentGroup.getName());
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_groupe_post_to_settingsGroupFragment, bundle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}