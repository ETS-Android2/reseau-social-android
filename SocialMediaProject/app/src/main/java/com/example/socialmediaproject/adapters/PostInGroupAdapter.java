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

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Post;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 4/26/21.
 *
 * Contrairement à Post Adapter, le title du post n'est pas le nom du groupe mais le nom de l'autheur
 */
public class PostInGroupAdapter extends RecyclerView.Adapter<PostInGroupAdapter.MyViewHolder> {

    // fields
    private Context context;
    private List<Post> postList;

    // constructor
    public PostInGroupAdapter(Context context, List<Post> postList){
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post currentItem = this.postList.get(position);

        holder.itemTitleView.setText(currentItem.getUserSender().getUsername());
        holder.itemSubtitleView.setText("");
        holder.itemContentView.setText(currentItem.getContent());
        holder.itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
        holder.itemNbViewsView.setText(String.valueOf(currentItem.getNbViews()));

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
                        // les 3 lignes suivantes permettent de supprimer l'item de la liste et du recyclerView
                        postList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,postList.size());
                        Toast.makeText(context, "Supprimer le post !"  , Toast.LENGTH_SHORT).show();
                        break;
                    case 2: // Partager
                        Toast.makeText(context, "Partager le post !"  , Toast.LENGTH_SHORT).show();
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });


        holder.likeButton.setOnClickListener(v -> {
            // on inverse l'état du like lors du clique sur le button
            currentItem.changeLike();
            if(currentItem.getIsLike()){ // si true alors
                holder.imageLike.setImageResource(R.drawable.ic_baseline_star_24);
            }else{
                holder.imageLike.setImageResource(R.drawable.ic_baseline_star_outline_24);
            }
            holder.itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView, itemSubtitleView, itemContentView, itemNbViewsView, itemNbStarsView;
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

            imageLike = itemView.findViewById(R.id.image_star);
            shareButton = itemView.findViewById(R.id.item_share);

            likeButton = itemView.findViewById(R.id.button_like_post);

        }
    }
}


