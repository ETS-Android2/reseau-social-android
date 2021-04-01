package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.PostItem;

import java.io.File;
import java.util.List;

/**
 * Created by Antoine Barbier on 3/30/21.
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
        String itemTitle = currentItem.getTitle();
        String itemSubtitle = currentItem.getSubtitle();
        String itemContent = currentItem.getContent();
        String itemNbViews = currentItem.getNbViews();
        String itemNbStars = currentItem.getNbStars();

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
        itemNbViewsView.setText(itemNbViews);

        // get item nb stars view
        TextView itemNbStarsView = (TextView) view.findViewById(R.id.item_nbStars);
        itemNbStarsView.setText(itemNbStars);

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

        return view;
    }
}
