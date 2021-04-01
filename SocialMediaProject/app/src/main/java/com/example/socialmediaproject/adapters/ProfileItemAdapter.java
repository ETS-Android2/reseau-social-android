package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.ProfileItem;

import java.util.List;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class ProfileItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<ProfileItem> profileItemList;
    private LayoutInflater inflater;

    // constructor
    public ProfileItemAdapter(Context context, List<ProfileItem> profileItemList){
        this.context = context;
        this.profileItemList = profileItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return profileItemList.size();
    }

    @Override
    public ProfileItem getItem(int position) {
        return profileItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_profile_item, null);

        // get informations about item
        ProfileItem currentItem = getItem(i);

        String itemTitle = currentItem.getTitle();

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);


        // accéder à la notification
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Voir : " + itemTitle , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
