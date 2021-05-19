package com.example.socialmediaproject.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;


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

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une action");

        if(this.groupTypeChat){
            if(currentUserIsAuthor){
                // Un appui long pour poucoir supprimer le message
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String[] actions = {"Supprimer"};
                        builder.setItems(actions, (dialog, which) -> {
                            if (which == 0) { // Supprimer
                                getSnapshots().getSnapshot(position).getReference().delete();
                                // notifyDataSetChanged();
                                Toast.makeText(context, "Suppression du post ! ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                    });
                }
        }else{
            holder.shareButton.setOnClickListener(v -> {

                GroupHelper.getGroup(model.getGroup()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Group postGroup = documentSnapshot.toObject(Group.class);

                        // add a list
                        if (currentUserIsAuthor) {
                            String[] actions = {"Modifier", "Supprimer"};
                            builder.setItems(actions, (dialog, which) -> {
                                switch (which) {
                                    case 0: // Modifier
                                        Toast.makeText(context, "Modifier le post !", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1: // Supprimer
                                        getSnapshots().getSnapshot(position).getReference().delete();
                                        // notifyDataSetChanged();
                                        Toast.makeText(context, "Supprimer le post : " + getSnapshots().getSnapshot(position).getReference().getId(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            });
                        } else if (postLayoutForGroup) {
                            // SI JE LE POST EST AFFICHER DANS UN GROUPE


                            assert postGroup != null;


                            // -> POSSIBILITÉ POUR UN ADMIN ET UN MODÉRATEUR DE SUPPRIMÉ LE POST

                            if (postGroup.getAdmin().equals(BaseActivity.getUid())) {
                                String[] actions = {"Supprimer"};
                                builder.setItems(actions, (dialog, which) -> {
                                    if (which == 0) { // Supprimer
                                        getSnapshots().getSnapshot(position).getReference().delete();
                                        // notifyDataSetChanged();
                                        Toast.makeText(context, "Supprimer le post : " + getSnapshots().getSnapshot(position).getReference().getId(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (postGroup.getModerators().contains(BaseActivity.getUid())
                                    && postGroup.getModerators().contains(model.getUserSender())) {
                                // Si la personne qui à posté n'est pas modérateur, alors un modérateur peut modérer son post

                                String[] actions = {"Supprimer"};
                                builder.setItems(actions, (dialog, which) -> {
                                    if (which == 0) { // Supprimer
                                        getSnapshots().getSnapshot(position).getReference().delete();
                                        // notifyDataSetChanged();
                                        Toast.makeText(context, "Supprimer le post : " + getSnapshots().getSnapshot(position).getReference().getId(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Je suis un utilisateur et je peux report le message
                                String[] actions = {"Report abuse"};
                                builder.setItems(actions, (dialog, which) -> {
                                    if (which == 0) { // Report abuse
                                        Toast.makeText(context, "Reporter le post ! (à faire)", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } else {
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
                    }
                });
            });

            // print picture into profile item
            ImageView imgProfile = holder.itemView.findViewById(R.id.item_icon);

            UserHelper.getUser(model.getUserSender()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                    User sender = task.getResult().toObject(User.class);

                    Glide.with(context)
                            .load(BaseActivity.getRefImg(sender.getUrlPicture()))
                            .into(imgProfile);
                }
            });

            // print picture into message content
            ImageView img = holder.itemView.findViewById(R.id.item_picture);
            String urlPic = model.getUrlImage();

            if(!model.getUrlImage().equals("null")){
                img.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(BaseActivity.getRefImg(model.getUrlImage()))
                        .into(img);
            }
            else{
                img.setVisibility(View.GONE);
            }

        }


    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitleView, itemSubtitleView, itemContentView, itemDateAgo;
        ImageButton shareButton;
        LinearLayout messageContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // get item title view
            messageContainer = itemView.findViewById(R.id.messageContainer);
            itemTitleView = itemView.findViewById(R.id.item_title);
            itemSubtitleView = itemView.findViewById(R.id.item_subtitle);
            itemContentView = itemView.findViewById(R.id.item_content);

            itemDateAgo = itemView.findViewById(R.id.item_date_ago);

            shareButton = itemView.findViewById(R.id.item_share);

        }

        public void updateWithPost(Post currentItem, boolean _postLayoutForGroup, boolean _groupTypeChat){

            itemContentView.setText(currentItem.getContent());
            itemDateAgo.setText(BaseActivity.getTimeAgo(currentItem.getDateCreated()));

            int colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorSecondary);
            int colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.colorLight);

            if(_groupTypeChat && _postLayoutForGroup){
                boolean currentUserIsAuthor = currentItem.getUserSender().equals(BaseActivity.getUid());
                //Update Message Color Background
                ((GradientDrawable) messageContainer.getBackground()).setColor(currentUserIsAuthor ? colorCurrentUser : colorRemoteUser);

                // MESSAGE CONTAINER
                LinearLayout.LayoutParams paramsLayoutContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsLayoutContent.setMargins(
                        currentUserIsAuthor ? 120 : 20,
                        10,
                        currentUserIsAuthor ? 20 : 120,
                        10);
                this.messageContainer.setLayoutParams(paramsLayoutContent);

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
