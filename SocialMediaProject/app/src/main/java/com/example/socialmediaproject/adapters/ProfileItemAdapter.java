package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = this.inflater.inflate(R.layout.adapter_profile_item, null);

        // get informations about item
        ProfileItem currentItem = getItem(i);

        String itemTitle = currentItem.getTitle();
        String mnenonic = currentItem.getMnenonic();

        // get item icon view
        ImageView itemIconView = view.findViewById(R.id.item_icon);
        String ressourceName = "ic_baseline_"+ mnenonic + "_24" ;
        int resId = context.getResources().getIdentifier(ressourceName, "drawable", context.getPackageName());
        itemIconView.setImageResource(resId);

        // get item title view
        TextView itemTitleView = (TextView) view.findViewById(R.id.item_title);
        itemTitleView.setText(itemTitle);


        View finalView = view;
        // accéder à la notification
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (itemTitle){
                    case "Mes favoris":
                        Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_myFavorisFragment);
                        break;
                    case "Mes posts":
                        Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_myPostsFragment);
                        break;
                    case "Mes groupes":
                        Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_myGroupsFragment);
                        break;
                    case "Mes informations":
                        Navigation.findNavController(finalView).navigate(R.id.action_navigation_profile_to_my_informationsFragment);
                        break;
                    default:
                        Toast.makeText(context, "Voir : " + itemTitle , Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        return view;
    }
}
