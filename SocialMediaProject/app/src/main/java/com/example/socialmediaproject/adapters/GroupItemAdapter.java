package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.GroupItem;

import java.util.List;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class GroupItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<GroupItem> groupItemList;
    private LayoutInflater inflater;

    // constructor
    public GroupItemAdapter(Context context, List<GroupItem> groupItemList){
        this.context = context;
        this.groupItemList = groupItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groupItemList.size();
    }

    @Override
    public GroupItem getItem(int position) {
        return groupItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_group_item, null);

        // get informations about item
        GroupItem currentItem = getItem(i);

        String itemTitle = currentItem.getTitle();

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);


        // accéder à la notification
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Voir le groupe : " + itemTitle , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
