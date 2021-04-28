package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.socialmediaproject.MainActivity;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_group_item, null);

        // get informations about item
        GroupItem currentItem = getItem(i);

        String itemName = currentItem.getName();
        String itemType = currentItem.getType();
        String itemAccess = currentItem.getName();
        String itemPublication = currentItem.getType();
        String itemSubject = currentItem.getType();

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemName);


        // accéder à la notification
        View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Voir le groupe : " + itemTitle , Toast.LENGTH_SHORT).show();

                // envoyer des données entre fragments
                Bundle bundle = new Bundle();
                bundle.putString("groupName", itemName);
                bundle.putString("groupType", itemType);
                bundle.putString("groupAccess", itemType);
                bundle.putString("groupPublication", itemType);
                bundle.putString("groupSubject", itemType);
                Navigation.findNavController(finalView).navigate(R.id.action_navigation_dashboard_to_navigation_groupe_post, bundle);
            }
        });



        return view;
    }
}
