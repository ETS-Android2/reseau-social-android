package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.ui.mes_reseaux.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * Created by Antoine Barbier on 5/19/21.
 */

public class GroupGestionAdapter extends FirestoreRecyclerAdapter<Group, GroupGestionAdapter.MyViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    protected Context context;

    //FOR DATA
    private final RequestManager glide;

    //FOR COMMUNICATION
    private Listener callback;


    // constructor
    public GroupGestionAdapter(@NonNull FirestoreRecyclerOptions<Group> options,
                              RequestManager glide,
                              Listener callback){
        super(options);
        this.glide = glide;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Group model){
        holder.updateWithPost(model, this.glide);

    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        Context context;
        TextView itemGroupNameView, itemGroupTypeView;
        Button button_add;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemGroupNameView = itemView.findViewById(R.id.group_name);
            itemGroupTypeView = itemView.findViewById(R.id.group_type);
            button_add = itemView.findViewById(R.id.button_add);
        }

        public void updateWithPost(Group group, RequestManager glide){
            Group currentItem = group;
            // Set Group Name View
            if(currentItem.getName() != null){
                itemGroupNameView.setText(currentItem.getName());
            }

            // Set Group Type View ...
            if(currentItem.getType() != null){
                itemGroupTypeView.setTextColor(Color.GRAY);
                itemGroupTypeView.setText(currentItem.getType().toUpperCase());
            }


            boolean isAdmin = currentItem.getAdmin().equals(BaseActivity.getUid());
            if(isAdmin){
                // si l'utilisateur est déja dans le groupe
                button_add.setEnabled(true);
                button_add.setText("Paramètres");
                button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("group_name", currentItem.getName());
                        Navigation.findNavController(itemView).navigate(R.id.settingsGroupFragment, bundle);

                    }
                });

            }

            // Lorsque l'on clique sur un item -> on accède au groupe
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("group_name", currentItem.getName());
                    bundle.putString("group_type", currentItem.getType());
                    if(currentItem.getType().equals("chat")){
                        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    }else {
                        Navigation.findNavController(itemView).navigate(R.id.navigation_groupe, bundle);
                    }

                }
            });
        }
    }
}
