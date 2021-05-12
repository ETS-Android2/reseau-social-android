package com.example.socialmediaproject.adapters;


import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 4/26/21.
 */
public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.MyViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Context context;

    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;


    // constructor
    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options,
                       RequestManager glide,
                       Listener callback, String idCurrentUser){
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Post model){
        holder.updateWithPost(model, this.idCurrentUser, this.glide);

        holder.shareButton.setOnClickListener(v -> {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choisir une action");
            // add a list
            String[] actions = {"Modifier", "Supprimer", "Partager"};
            builder.setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0: // Modifier
                        Toast.makeText(context, "Modifier le post !"  , Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // Supprimer
                        getSnapshots().getSnapshot(position).getReference().delete();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Supprimer le post : "+ getSnapshots().getSnapshot(position).getReference().getId()  , Toast.LENGTH_SHORT).show();
                        break;
                    case 2: // Partager
                        Toast.makeText(context, "Partager le post !"  , Toast.LENGTH_SHORT).show();
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView, itemSubtitleView, itemContentView, itemNbViewsView, itemNbStarsView, itemDateAgo;
        ImageView imageLike;
        ImageButton shareButton;
        LinearLayout likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // get item title view
            itemTitleView = itemView.findViewById(R.id.item_title);
            itemSubtitleView = itemView.findViewById(R.id.item_subtitle);
            itemContentView = itemView.findViewById(R.id.item_content);
            itemNbViewsView = itemView.findViewById(R.id.item_nbViews);
            itemNbStarsView = itemView.findViewById(R.id.item_nbStars);

            itemDateAgo = itemView.findViewById(R.id.item_date_ago);


            imageLike = itemView.findViewById(R.id.image_star);
            shareButton = itemView.findViewById(R.id.item_share);

            likeButton = itemView.findViewById(R.id.button_like_post);
        }

        public void updateWithPost(Post post, String currentUserId, RequestManager glide){
            Post currentItem = post;



            if(currentItem.getGroup() == null){
                itemTitleView.setText("null");
            }else{
                itemTitleView.setText(currentItem.getGroup());
            }
            if(currentItem.getUserSender() == null){
                itemSubtitleView.setText("null");
            }else{
                itemSubtitleView.setText(currentItem.getUserSender());
            }
            itemContentView.setText(currentItem.getContent());
            itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
            itemNbViewsView.setText(String.valueOf(currentItem.getNbViews()));

            itemDateAgo.setText(String.valueOf(currentItem.getTimeAgo()));


            likeButton.setOnClickListener(v -> {
                // on inverse l'état du like lors du clique sur le button
                currentItem.changeLike();
                if(currentItem.getIsLike()){ // si true alors
                   imageLike.setImageResource(R.drawable.ic_baseline_star_24);
                }else{
                    imageLike.setImageResource(R.drawable.ic_baseline_star_outline_24);
                }
                itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
            });
        }
    }
}
