package com.example.socialmediaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.newPostActivity;
import com.example.socialmediaproject.ui.mes_reseaux.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 4/26/21.
 */
public class GroupAdapter extends FirestoreRecyclerAdapter<Group, GroupAdapter.MyViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Context context;

    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;

    public GroupAdapter(@NonNull FirestoreRecyclerOptions<Group> options, RequestManager glide, Listener callback, String idCurrentUser) {
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Group model) {

        holder.updateWithMessage(model, this.idCurrentUser, this.glide);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_group_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView;
        ImageView iv_imageAccess;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitleView = itemView.findViewById(R.id.item_title);
            iv_imageAccess = itemView.findViewById(R.id.groupItem_access_image);

        }

        public void updateWithMessage(Group model, String currentUserId, RequestManager glide){

            itemTitleView.setText(model.getName());

            // Affichage de l'icone cadena en fonction du group privé ou public
            if(model.getAccessPrivate()){
                iv_imageAccess.setImageResource(R.drawable.ic_baseline_lock_24);
            }else{
                iv_imageAccess.setImageResource(R.drawable.ic_baseline_lock_open_24);
            }


            // accéder à la notification
            itemView.setOnClickListener(v -> {
                //Toast.makeText(context, "Voir le groupe : " + itemTitle , Toast.LENGTH_SHORT).show();
                if(model.getType().equals("chat")){
                    // Si on est dans un groupe de type CHAT alors on ouvre le chat
                    Bundle bundle = new Bundle();
                    bundle.putString("group_name", model.getName());
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }else{
                    // Sinon on lance le fragment classique d'un groupe
                    Bundle bundle = new Bundle();
                    bundle.putString("group_name", model.getName());
                    Navigation.findNavController(itemView).navigate(R.id.action_navigation_dashboard_to_navigation_groupe_post, bundle);
                }

            });
        }
    }
}
