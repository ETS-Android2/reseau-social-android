package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.PostItem;

import java.io.File;
import java.util.List;

/**
 * Created by Antoine Barbier on 4/26/21.
 *
 * Contrairement à Post Adapter, le title du post n'est pas le nom du groupe mais le nom de l'autheur
 */
public class PostInGroupAdapter extends RecyclerView.Adapter<PostInGroupAdapter.MyViewHolder> {

    // fields
    private Context context;
    private List<PostItem> postList;

    // constructor
    public PostInGroupAdapter(Context context, List<PostItem> postList){
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
        PostItem currentItem = this.postList.get(position);

        holder.itemTitleView.setText(currentItem.getAuthor());
        holder.itemSubtitleView.setText("");
        holder.itemContentView.setText(currentItem.getContent());
        holder.itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
        holder.itemNbViewsView.setText(String.valueOf(currentItem.getNbViews()));

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String akpath = context.getApplicationInfo().sourceDir;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(akpath)));
                context.startActivity(Intent.createChooser(intent,"Share post"));
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on inverse l'état du like lors du clique sur le button
                currentItem.changeLike();
                if(currentItem.getIsLike()){ // si true alors
                    holder.imageLike.setImageResource(R.drawable.ic_baseline_star_24);
                }else{
                    holder.imageLike.setImageResource(R.drawable.ic_baseline_star_outline_24);
                }
                holder.itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
            }
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
            itemTitleView = (TextView) itemView.findViewById(R.id.item_title);
            itemSubtitleView = (TextView) itemView.findViewById(R.id.item_subtitle);
            itemContentView = (TextView) itemView.findViewById(R.id.item_content);
            itemNbViewsView = (TextView) itemView.findViewById(R.id.item_nbViews);
            itemNbStarsView = (TextView) itemView.findViewById(R.id.item_nbStars);

            imageLike = (ImageView) itemView.findViewById(R.id.image_star);
            shareButton = (ImageButton) itemView.findViewById(R.id.item_share);

            likeButton = (LinearLayout) itemView.findViewById(R.id.button_like_post);

        }
    }
}


