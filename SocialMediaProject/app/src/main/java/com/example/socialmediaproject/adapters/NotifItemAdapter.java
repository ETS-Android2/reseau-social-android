package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.NotifItem;

import java.util.List;

/**
 * Created by Antoine Barbier on 3/30/21.
 */
public class NotifItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<NotifItem> notifItemList;
    private LayoutInflater inflater;

    // constructor
    public NotifItemAdapter(Context context, List<NotifItem> notifItemList){
        this.context = context;
        this.notifItemList = notifItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notifItemList.size();
    }

    @Override
    public NotifItem getItem(int position) {
        return notifItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_notif_item, null);

        // get informations about item
        NotifItem currentItem = getItem(i);

        String itemType = currentItem.getType();
        String itemTitle = currentItem.getTitle();
        String itemContent = currentItem.getContent();


        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);

        // get item content view
        TextView itemContentView = (TextView) view.findViewById(R.id.item_content);
        itemContentView.setText(itemContent);

        View finalView = view;
        // accéder à la notification
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Voir la notification : " + itemTitle , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
