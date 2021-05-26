package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Notif;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class NotifItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<Notif> notifItemList;
    private LayoutInflater inflater;

    // constructor
    public NotifItemAdapter(Context context, List<Notif> notifItemList){
        this.context = context;
        this.notifItemList = notifItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notifItemList.size();
    }

    @Override
    public Notif getItem(int position) {
        return notifItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_notif_item, null);

        // get informations about item
        Notif currentItem = getItem(i);

        String itemType = currentItem.getType();
        String itemTitle = currentItem.getTitle();
        String itemContent = currentItem.getContent();


        // get item title view
        TextView itemTitleView = view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);

        // get item content view
        TextView itemContentView = view.findViewById(R.id.item_content);
        itemContentView.setText(itemContent);

        View finalView = view;
        // accéder à la notification
        //view.setOnClickListener(v -> Toast.makeText(context, "Voir la notification : " + itemTitle , Toast.LENGTH_SHORT).show());

        return view;
    }
}
