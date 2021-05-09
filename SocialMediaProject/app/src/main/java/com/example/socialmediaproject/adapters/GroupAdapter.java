package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Group;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 4/26/21.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    // fields
    private Context context;
    private List<Group> groupList;

    //constructor
    public GroupAdapter(Context context, List<Group> groupList){
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_group_item, parent, false);
        return new GroupAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group currentItem = this.groupList.get(position);

        holder.itemTitleView.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return this.groupList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitleView = itemView.findViewById(R.id.item_title);


            // accéder à la notification
            itemView.setOnClickListener(v -> {
                //Toast.makeText(context, "Voir le groupe : " + itemTitle , Toast.LENGTH_SHORT).show();
                Navigation.findNavController(itemView).navigate(R.id.action_navigation_dashboard_to_navigation_groupe_post);
            });


        }
    }
}
