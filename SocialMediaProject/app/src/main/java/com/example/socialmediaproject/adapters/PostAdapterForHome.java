package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;

import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;

import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.ImageFullScreenActivity;
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
        View view = inflater.inflate(R.layout.adapter_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.messageContainer.setVisibility(View.GONE);

        holder.shareButton.setVisibility(View.GONE);
        boolean currentUserIsAuthor = postList.get(position).getUserSender().equals(BaseActivity.getUid());

        GroupHelper.getGroup(postList.get(position).getGroup()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group postGroup = documentSnapshot.toObject(Group.class);
                assert postGroup != null;

                holder.updateWithPost(postList.get(position));

                /**
                 * Colour background text clickable lorsque l'on click dessus
                 */
                holder.itemTitleView.setBackgroundResource(R.drawable.selector_onclick);

                /**
                 * NAVIGATION VERS LE GROUPE LORSQUE L'ON CLICK SUR LE NOM DU GROUPE
                 *
                 */
                holder.itemTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("group_name", postList.get(position).getGroup());
                        Navigation.findNavController(holder.itemView).navigate(R.id.navigation_groupe, bundle);
                    }
                });

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

                                    /**
                                     * ON AFFICHE L'IMAGE EN FULL SCREEN LORS DU CLICK
                                     */
                                    fullScreenImage(currentItem.getUrlImage());

                                }
                                else{
                                    imgContent.setVisibility(View.GONE);
                                }


                            }
                        });

                    }});

            }


            private void fullScreenImage(String url){
                imgContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("urlImage", url);
                        Intent intent = new Intent(itemView.getContext(), ImageFullScreenActivity.class);
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

    }


}

