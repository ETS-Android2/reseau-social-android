package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnSuccessListener;

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

    private boolean adapterForWaitlist;

    // constructor
    public UserAdapter(Context context, List<User> userList, Group group){
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
        this.currentGroup = group;
        this.adapterForWaitlist = false;
    }

    public UserAdapter(Context context, List<User> userList, Group group, Boolean adapterForwaitlist){
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
        this.currentGroup = group;
        this.adapterForWaitlist = adapterForwaitlist;
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

    @SuppressLint({"ViewHolder", "InflateParams", "ResourceAsColor"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // get informations about item
        User currentItem = getItem(i);

        // si on veut l'adapteur pour la waitlist alors on charge le layout en question
        if(this.adapterForWaitlist){
            view = this.inflater.inflate(R.layout.adapter_user_waitlist_item, null);

            // get item name view
            TextView itemNameView =  view.findViewById(R.id.user_name);
            itemNameView.setText(currentItem.getUsername());

            Button button_add =  view.findViewById(R.id.button_add);
            ImageButton button_delete =  view.findViewById(R.id.button_delete);

            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupHelper.addUserInGroup(currentGroup.getName(),currentItem.getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //on le supprime de la liste d'attente et on l'ajoute dans la liste des membres
                                    currentGroup.removeFromWaitlist(currentItem.getUid());
                                    userList.remove(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Utilisateur ajouté !", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupHelper.removeUserFromWaitlistGroup(currentGroup.getName(),currentItem.getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //on le supprime de la liste d'attente et on l'ajoute dans la liste des membres
                                    currentGroup.removeFromWaitlist(currentItem.getUid());
                                    userList.remove(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Utilisateur supprimé de la liste d'attente! ", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });


        }else{
            view = this.inflater.inflate(R.layout.adapter_user_item, null);

            // get item name view
            TextView itemNameView = (TextView) view.findViewById(R.id.user_name);
            itemNameView.setText(currentItem.getUsername());

            TextView itemRoleView = (TextView) view.findViewById(R.id.user_role);
            // on écrit le role de l'utilisateur
            if(this.isAdmin(currentItem.getUid())){
                itemRoleView.setText("Admin");
                itemRoleView.setTextColor(Color.RED);
            }else if(this.isModerator(currentItem.getUid())){
                itemRoleView.setText("Moderator");
                itemRoleView.setTextColor(Color.parseColor("#388E3C"));
                dialogForModerator(view, i, currentItem);
            }else{
                itemRoleView.setText("Member");
                itemRoleView.setTextColor(Color.GRAY);
                dialogForMember(view, i, currentItem);
            }
        }





        return view;
    }

    private boolean isAdmin(String uid){
        return currentGroup.getAdmin().equals(uid);
    }

    private boolean isModerator(String uid){
        return currentGroup.getModerators().contains(uid);
    }

    private void changeToMember(String uid, View view){
        TextView itemRoleView = view.findViewById(R.id.user_role);
        itemRoleView.setText("Member");
        itemRoleView.setTextColor(Color.GRAY);
    }

    private void changeToModerator(String uid, View view){
        TextView itemRoleView = view.findViewById(R.id.user_role);
        itemRoleView.setText("Moderator");
        itemRoleView.setTextColor(Color.parseColor("#388E3C"));
    }

    private void dialogForModerator(View view, int position, User currentItem){
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
                                        changeToMember(currentItem.getUid(), view);
                                        currentGroup.removeToModerators(currentItem.getUid());
                                        dialogForMember(view, position, currentItem);
                                        Toast.makeText(context, "Retrograder à simple membre !", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        break;
                    case 2: // Exclure l'utilisateur
                        GroupHelper.removeUserFromGroup(currentGroup.getName(),currentItem.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        currentGroup.removeToModerators(currentItem.getUid());
                                        currentGroup.removeToMembers(currentItem.getUid());
                                        userList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Exclure l'utilisateur !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void dialogForMember(View view, int position, User currentItem){
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
                                        changeToModerator(currentItem.getUid(), view);
                                        currentGroup.addToModerators(currentItem.getUid());
                                        dialogForModerator(view, position, currentItem);
                                        Toast.makeText(context, "Promouvoir au role de modérateur !"  , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        break;
                    case 2: // Exclure l'utilisateur
                        GroupHelper.removeUserFromGroup(currentGroup.getName(),currentItem.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        currentGroup.removeToModerators(currentItem.getUid());
                                        currentGroup.removeToMembers(currentItem.getUid());
                                        userList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Exclure l'utilisateur !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

}
