package com.example.socialmediaproject.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 4/26/21.
 */
public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.MyViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Context context;

    private boolean postLayoutForGroup;
    private boolean groupTypeChat; // si Afficher dans un groupe style WhatsApp
    //FOR DATA
    private final RequestManager glide;

    //FOR COMMUNICATION
    private Listener callback;


    // constructor
    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options,
                       RequestManager glide,
                       Listener callback){
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.postLayoutForGroup = false;
        this.groupTypeChat = false;
    }

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options,
                       RequestManager glide,
                       Listener callback, boolean postLayoutForGroup){
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.postLayoutForGroup = postLayoutForGroup;
        this.groupTypeChat = false;
    }

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options,
                       RequestManager glide,
                       Listener callback, boolean postLayoutForGroup, boolean groupTypeChat){
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.postLayoutForGroup = postLayoutForGroup;
        this.groupTypeChat = groupTypeChat;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if(this.groupTypeChat){
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_message_for_chat, parent, false));
        }else{
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_post_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Post model){
        holder.updateWithPost(model, postLayoutForGroup, groupTypeChat);


        boolean currentUserIsAuthor = model.getUserSender().equals(BaseActivity.getUid());

        if(this.groupTypeChat){
            // On fait un truc s
        }else{
            holder.shareButton.setOnClickListener(v -> {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choisir une action");

                // add a list
                if(currentUserIsAuthor){
                    String[] actions = {"Modifier", "Supprimer"};
                    builder.setItems(actions, (dialog, which) -> {
                        switch (which) {
                            case 0: // Modifier
                                Toast.makeText(context, "Modifier le post !"  , Toast.LENGTH_SHORT).show();
                                break;
                            case 1: // Supprimer
                                getSnapshots().getSnapshot(position).getReference().delete();
                                // notifyDataSetChanged();
                                Toast.makeText(context, "Supprimer le post : "+ getSnapshots().getSnapshot(position).getReference().getId()  , Toast.LENGTH_SHORT).show();
                                break;
                        } });
                }else if(postLayoutForGroup){
                    // SI JE LE POST EST AFFICHER DANS UN GROUPE

                    // -> POSSIBILITÉ POUR UN ADMIN ET UN MODÉRATEUR DE SUPPRIMÉ LE POST
                    if(false){
                        String[] actions = {"Supprimer"};
                        builder.setItems(actions, (dialog, which) -> {
                            if (which == 0) { // Supprimer
                                getSnapshots().getSnapshot(position).getReference().delete();
                                // notifyDataSetChanged();
                                Toast.makeText(context, "Supprimer le post : " + getSnapshots().getSnapshot(position).getReference().getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    String[] actions = {"Report abuse"};
                    builder.setItems(actions, (dialog, which) -> {
                        if (which == 0) { // Report abuse
                            Toast.makeText(context, "Reporter le post ! (à faire)", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }


    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView, itemSubtitleView, itemContentView, itemDateAgo;
        ImageButton shareButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // get item title view
            itemTitleView = itemView.findViewById(R.id.item_title);
            itemSubtitleView = itemView.findViewById(R.id.item_subtitle);
            itemContentView = itemView.findViewById(R.id.item_content);

            itemDateAgo = itemView.findViewById(R.id.item_date_ago);

            shareButton = itemView.findViewById(R.id.item_share);

        }

        public void updateWithPost(Post currentItem, boolean _postLayoutForGroup, boolean _groupTypeChat){

            itemContentView.setText(currentItem.getContent());
            itemDateAgo.setText(BaseActivity.getTimeAgo(currentItem.getDateCreated()));

            if(_groupTypeChat && _postLayoutForGroup){

                UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        itemTitleView.setVisibility(View.VISIBLE);
                        itemTitleView.setText(documentSnapshot.toObject(User.class).getUsername());
                    }
                });

            }else{
                // title
                if(currentItem.getGroup() == null){
                    itemTitleView.setText("");
                }else{
                    itemTitleView.setVisibility(View.GONE);
                    if(_postLayoutForGroup){
                        // si c'est le post qui est afficher dans un group alors on affiche le nom de l'utilisateur dans le titre
                        UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                itemTitleView.setVisibility(View.VISIBLE);
                                itemTitleView.setText(documentSnapshot.toObject(User.class).getUsername());
                            }
                        });
                    }else{
                        // sinon on affiche le nom du groupe
                        itemTitleView.setVisibility(View.VISIBLE);
                        itemTitleView.setText(currentItem.getGroup());
                    }

                }

                // subtitle
                if(currentItem.getUserSender() == null){
                    itemSubtitleView.setText("");
                }else{
                    // tant qu'on a pas charger les données on affiche rien
                    itemSubtitleView.setVisibility(View.GONE);
                    if(_postLayoutForGroup){
                        // si c'est le post qui est afficher dans un group alors on le role de l'utilisateur
                        GroupHelper.getGroup(currentItem.getGroup()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemSubtitleView.setVisibility(View.VISIBLE);

                                Group groupPost = documentSnapshot.toObject(Group.class);
                                boolean isAdmin  = groupPost.getAdmin().equals(currentItem.getUserSender());
                                boolean isModerator  = groupPost.getModerators().contains(currentItem.getUserSender());
                                boolean isMember  = groupPost.getMembers().contains(currentItem.getUserSender());
                                itemSubtitleView.setTextColor(Color.GRAY);
                                if(isAdmin){
                                    itemSubtitleView.setText("Admin");
                                }else if(isModerator){
                                    itemSubtitleView.setText("Moderator");
                                }else if(isMember){
                                    itemSubtitleView.setText("Member");
                                }else{
                                    itemSubtitleView.setText("Former member (n'est plus dans le group)");
                                }
                            }
                        });
                    }else{
                        // sinon on affiche le nom de l'utilisateur
                        UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemSubtitleView.setVisibility(View.VISIBLE);
                                itemSubtitleView.setText(documentSnapshot.toObject(User.class).getUsername());
                            }
                        });
                    }
                }
            }

        }
    }
}
