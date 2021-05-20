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
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;

import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;

import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/19/21.
 */

public class PostAdapterForHome extends RecyclerView.Adapter<PostAdapterForHome.ViewHolder> {

    private Context context;
    private ArrayList<Post> postList;

    public PostAdapterForHome(ArrayList<Post> postList){
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater
                .inflate(R.layout.adapter_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.messageContainer.setVisibility(View.GONE);

        boolean currentUserIsAuthor = postList.get(position).getUserSender().equals(BaseActivity.getUid());

        GroupHelper.getGroup(postList.get(position).getGroup()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group postGroup = documentSnapshot.toObject(Group.class);
                assert postGroup != null;

                holder.updateWithPost(postList.get(position));

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Choisir une action");

                // si je suis l'utilisateur qui à posté
                if(currentUserIsAuthor){
                    // Un appui long pour poucoir supprimer le message
                    holder.shareButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] actions = {"Supprimer"};
                            builder.setItems(actions, (dialog, which) -> {
                                if (which == 0) { // Supprimer
                                    //getSnapshots().getSnapshot(position).getReference().delete();
                                    // notifyDataSetChanged();
                                    Toast.makeText(context, "Suppression du post ! (à faire) ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // create and show the alert dialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else{

                // SINON ON VA REGARDER QU'ELLE ROLE ON POSS7DE DANS CE GROUPE
                    // -> POSSIBILITÉ POUR UN ADMIN ET UN MODÉRATEUR DE SUPPRIMÉ LE POST

                    if(postGroup.getAdmin().equals(BaseActivity.getUid())){
                        String[] actions = {"Supprimer"};
                        builder.setItems(actions, (dialog, which) -> {
                            if (which == 0) { // Supprimer
                                //getSnapshots().getSnapshot(position).getReference().delete();
                                // notifyDataSetChanged();
                                Toast.makeText(context, "Supprimer le post : (à faire)", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(postGroup.getModerators().contains(BaseActivity.getUid())
                            && postGroup.getModerators().contains(postList.get(position).getUserSender())){
                        // Si la personne qui à posté n'est pas modérateur, alors un modérateur peut modérer son post

                        String[] actions = {"Supprimer"};
                        builder.setItems(actions, (dialog, which) -> {
                            if (which == 0) { // Supprimer
                                // notifyDataSetChanged();
                                Toast.makeText(context, "Supprimer le post : (à faire)", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        // Je suis un utilisateur et je peux report le message
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

            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout messageContainer;
        TextView itemTitleView, itemSubtitleView, itemContentView, itemDateAgo;
        ImageView imgContent, imgProfile;
        ImageButton shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageContainer = itemView.findViewById(R.id.container);
            // get item title view
            itemTitleView = itemView.findViewById(R.id.item_title);
            itemSubtitleView = itemView.findViewById(R.id.item_subtitle);
            itemContentView = itemView.findViewById(R.id.item_content);

            //images
            imgContent = itemView.findViewById(R.id.item_picture);
            imgProfile = itemView.findViewById(R.id.item_icon);

            itemDateAgo = itemView.findViewById(R.id.item_date_ago);

            shareButton = itemView.findViewById(R.id.item_share);
        }
/*
        public void updateWithPost(Post currentItem){

            itemContentView.setText(currentItem.getContent());
            itemDateAgo.setText(BaseActivity.getTimeAgo(currentItem.getDateCreated()));

            // title
            if(currentItem.getGroup() == null){
                itemTitleView.setText("");
            }else{
                itemTitleView.setVisibility(View.VISIBLE);
                itemTitleView.setText(currentItem.getGroup());
            }

            // subtitle
            if(currentItem.getUserSender() == null){
                itemSubtitleView.setText("");
            }else{
                // tant qu'on a pas charger les données on affiche rien
                itemSubtitleView.setVisibility(View.GONE);

                // sinon on affiche le nom de l'utilisateur
                UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        itemSubtitleView.setVisibility(View.VISIBLE);
                        itemSubtitleView.setText(documentSnapshot.toObject(User.class).getUsername());
                    }
                });

            }


            UserHelper.getUser(currentItem.getUserSender()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                    User sender = task.getResult().toObject(User.class);

                    Glide.with(itemView.getContext())
                            .load(BaseActivity.getRefImg(sender.getUrlPicture()))
                            .into(imgProfile);
                }
            });

            // print picture into message content
            if(!currentItem.getUrlImage().equals("null")){
                imgContent.setVisibility(View.VISIBLE);

                Glide.with(itemView.getContext())
                        .load(BaseActivity.getRefImg(currentItem.getUrlImage()))
                        .into(imgContent);
            }
            else{
                imgContent.setVisibility(View.GONE);
            }

        }
*/

        public void updateWithPost(Post currentItem){

                // On récupère le nom du groupe
                GroupHelper.getGroup(currentItem.getGroup()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Group currentGroup =  documentSnapshot.toObject(Group.class);

                        // On recupère le nom de l'utilisateur et l'image
                        UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User currentUser =  documentSnapshot.toObject(User.class);
                                assert currentUser != null;

                                /**
                                 * ON REND VISIBLE LE LAYOUT
                                 */
                                messageContainer.setVisibility(View.VISIBLE);

                                // On place le texte du post dans le layout
                                // On place le temps écoulé depuis l'envoie du post
                                itemContentView.setText(currentItem.getContent());
                                itemDateAgo.setText(BaseActivity.getTimeAgo(currentItem.getDateCreated()));

                                // title

                                    itemTitleView.setText(currentItem.getGroup());


                                // subtitle
                                if(currentItem.getUserSender() == null){
                                    itemSubtitleView.setText("");
                                }else{

                                        // sinon on affiche le nom de l'utilisateur
                                        itemSubtitleView.setText(currentUser.getUsername());

                                }

                                UserHelper.getUser(currentItem.getUserSender()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                                        User sender = task.getResult().toObject(User.class);

                                        Glide.with(itemView.getContext())
                                                .load(BaseActivity.getRefImg(sender.getUrlPicture()))
                                                .into(imgProfile);
                                    }
                                });

                                // print picture into message content
                                if(!currentItem.getUrlImage().equals("null")){
                                    imgContent.setVisibility(View.VISIBLE);

                                    Glide.with(itemView.getContext())
                                            .load(BaseActivity.getRefImg(currentItem.getUrlImage()))
                                            .into(imgContent);
                                }
                                else{
                                    imgContent.setVisibility(View.GONE);
                                }


                            }
                        });

                    }});

            }



    }
}

