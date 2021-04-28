package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.PostItem;

import java.io.File;
import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class PostItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<PostItem> postItemList;
    private LayoutInflater inflater;

    // constructor
    public PostItemAdapter(Context context, List<PostItem> postItemList){
        this.context = context;
        this.postItemList = postItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postItemList.size();
    }

    @Override
    public PostItem getItem(int position) {
        return postItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.adapter_post_item, null);

        // get informations about item
        PostItem currentItem = getItem(i);
        String itemTitle = currentItem.getGroup().getName();
        String itemSubtitle = currentItem.getAuthor();
        String itemContent = currentItem.getContent();
        int itemNbViews = currentItem.getNbViews();
        int itemNbStars = currentItem.getNbStars();

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);

        // get item subtitle view
        TextView itemSubtitleView = (TextView) view.findViewById(R.id.item_subtitle);
        itemSubtitleView.setText(itemSubtitle);

        // get item content view
        TextView itemContentView = (TextView) view.findViewById(R.id.item_content);
        itemContentView.setText(itemContent);

        // get item nb views view
        TextView itemNbViewsView = (TextView) view.findViewById(R.id.item_nbViews);
        itemNbViewsView.setText(String.valueOf(itemNbViews));

        // get item nb stars view
        TextView itemNbStarsView = (TextView) view.findViewById(R.id.item_nbStars);
        itemNbStarsView.setText(String.valueOf(itemNbStars));

        ImageView imageLike = (ImageView) view.findViewById(R.id.image_star);
        if(currentItem.getIsLike()){ // si true alors
            imageLike.setImageResource(R.drawable.ic_baseline_star_24);
        }else{
            imageLike.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }

        // bouton partager la publication
        ImageButton shareButton = (ImageButton) view.findViewById(R.id.item_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String akpath = context.getApplicationInfo().sourceDir;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(akpath)));
                context.startActivity(Intent.createChooser(intent,"Share post"));
            }
        });

        // bouton "aimé" la publication
        LinearLayout likeButton = (LinearLayout) view.findViewById(R.id.button_like_post);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on inverse l'état du like lors du clique sur le button
                currentItem.changeLike();
                if(currentItem.getIsLike()){ // si true alors
                    imageLike.setImageResource(R.drawable.ic_baseline_star_24);
                }else{
                    imageLike.setImageResource(R.drawable.ic_baseline_star_outline_24);
                }
                itemNbStarsView.setText(String.valueOf(currentItem.getNbStars()));
            }
        });

        return view;
    }
}
