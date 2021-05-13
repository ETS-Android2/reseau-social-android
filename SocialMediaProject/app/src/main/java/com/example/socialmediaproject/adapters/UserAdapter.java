package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class UserAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;
    private Group currentGroup;

    // constructor
    public UserAdapter(Context context, List<User> userList, Group group){
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
        this.currentGroup = group;

    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.adapter_user_item, null);

        // get informations about item
        User currentItem = getItem(i);
        String itemName = currentItem.getUsername();

        // get item name view
        TextView itemNameView = (TextView) view.findViewById(R.id.user_name);
        itemNameView.setText(itemName);

        // si l'utilisateur selectionné est l'admin
        if(isAdmin(currentItem.getUid())){
            // on ne fait rien
        }else if(isModerator(currentItem.getUid())){ // si l'utilisateur est modérateur
            view.setOnClickListener(v -> {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Actions");
                // add a list
                String[] actions = {"Contact", "Demote to member", "Kick Out"};
                builder.setItems(actions, (dialog, which) -> {
                    switch (which) {
                        case 0: // Contacter
                            Toast.makeText(context, "Contacter l'utilisateur !"  , Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Retrograder à simple membre
                            GroupHelper.demoteModeratorToMember(currentGroup.getName(),currentItem.getUid())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Retrograder à simple membre !", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            break;
                        case 2: // Exclure l'utilisateur
                            GroupHelper.remoreUserFromGroup(currentGroup.getName(),currentItem.getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Exclure l'utilisateur !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            break;
                    } }); // create and show the alert dialog

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }else{ // sinon l'utilisateur est un utilisateur lambda
            view.setOnClickListener(v -> {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Actions");
                // add a list
                String[] actions = {"Contact", "Promote to Moderator", "Kick Out"};
                builder.setItems(actions, (dialog, which) -> {
                    switch (which) {
                        case 0: // Contacter
                            Toast.makeText(context, "Contacter l'utilisateur !"  , Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Promouvoir au role de modérateur
                            GroupHelper.promoteMemberToModerator(currentGroup.getName(),currentItem.getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Promouvoir au role de modérateur !"  , Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            break;
                        case 2: // Exclure l'utilisateur
                            GroupHelper.remoreUserFromGroup(currentGroup.getName(),currentItem.getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Exclure l'utilisateur !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            break;
                    } }); // create and show the alert dialog

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }


        return view;
    }

    private boolean isAdmin(String uid){
        return currentGroup.getAdmin().equals(uid);
    }

    private boolean isModerator(String uid){
        return currentGroup.getModerators().contains(uid);
    }

}
