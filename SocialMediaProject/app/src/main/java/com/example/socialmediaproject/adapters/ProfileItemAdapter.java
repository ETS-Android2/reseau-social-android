package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.ProfileItem;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
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

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_profile_item, null);

        // get informations about item
        ProfileItem currentItem = getItem(i);

        String itemTitle = currentItem.getTitle();
        String mnenonic = currentItem.getMnenonic();
        int itemPosition = currentItem.getItem();

        // get item icon view
        ImageView itemIconView = view.findViewById(R.id.item_icon);
        String ressourceName = "ic_baseline_"+ mnenonic + "_24" ;
        int resId = context.getResources().getIdentifier(ressourceName, "drawable", context.getPackageName());
        itemIconView.setImageResource(resId);

        // modifier la couleur des icones
        //itemIconView.setColorFilter(ContextCompat.getColor(context, R.color.colorSecondary),PorterDuff.Mode.MULTIPLY);

        // get item title view
        TextView itemTitleView = view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);


        View finalView = view;
        // accéder à la notification
        view.setOnClickListener(v -> {

            switch (itemPosition){
                case 0:
                    Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_navigation_newGroup);
                    break;
                case 1:
                    Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_myPostsFragment);
                    break;
                case 2:
                    Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_myGroupsFragment);
                    break;
                case 3:
                    Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_my_informationsFragment);
                    break;
                default:
                    break;
            }

        });

        return view;
    }
}
