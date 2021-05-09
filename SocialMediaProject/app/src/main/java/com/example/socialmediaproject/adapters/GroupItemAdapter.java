package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.enums.Publication;
import com.example.socialmediaproject.models.Group;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class GroupItemAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<Group> groupItemList;
    private LayoutInflater inflater;

    // constructor
    public GroupItemAdapter(Context context, List<Group> groupItemList){
        this.context = context;
        this.groupItemList = groupItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groupItemList.size();
    }

    @Override
    public Group getItem(int position) {
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
        Group currentItem = getItem(i);

        String itemName = currentItem.getName();
        String itemType = currentItem.getType();
        Access itemAccess = currentItem.getAccess();
        Publication itemPublication = currentItem.getPublication();
        String itemSubject = currentItem.getField();

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemName);

        // Affichage de l'icon cadena en fonction du group privé ou public
        ImageView iv_imageAccess = (ImageView) view.findViewById(R.id.groupItem_access_image);
        if(currentItem.isPrivate()){
            iv_imageAccess.setImageResource(R.drawable.ic_baseline_lock_24);
        }else{

            iv_imageAccess.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }


        // accéder à la notification
        View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Voir le groupe : " + itemTitle , Toast.LENGTH_SHORT).show();

                // envoyer l'objet group dans le fragment de destination
                Bundle bundle = new Bundle();
                Group obj = currentItem;
                bundle.putSerializable("group", (Serializable) obj);

                Navigation.findNavController(finalView).navigate(R.id.action_navigation_dashboard_to_navigation_groupe_post, bundle);
            }
        });



        return view;
    }
}
