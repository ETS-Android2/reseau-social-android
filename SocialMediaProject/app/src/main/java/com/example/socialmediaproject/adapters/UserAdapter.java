package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
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
                                    //Toast.makeText(context, context.getResources().getString(R.string.toast_send_request_addUser), Toast.LENGTH_SHORT).show();
                                    if(userList.size()==0){
                                        ((Activity) viewGroup.getContext()).onBackPressed();
                                    }
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
                                    //Toast.makeText(context, context.getResources().getString(R.string.toast_delete_user_waitlist), Toast.LENGTH_SHORT).show();
                                    if(userList.size()==0){
                                        ((Activity) viewGroup.getContext()).onBackPressed();
                                    }
                                }
                            });

                }
            });


        }else{
            view = this.inflater.inflate(R.layout.adapter_user_item, null);

            // le layout
            LinearLayout user_layout = view.findViewById(R.id.user_layout);

            // Si l'utilisateur de la liste est l'utilisateur connectés
            if(currentItem.getUid().equals(BaseActivity.getUid())){
                user_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.fui_transparent));
            }

            // get item name view
            TextView itemNameView = (TextView) view.findViewById(R.id.user_name);
            itemNameView.setText(currentItem.getUsername());

            TextView itemRoleView = (TextView) view.findViewById(R.id.user_role);


            // on écrit le role de l'utilisateur
            if(this.isAdmin(currentItem.getUid())){
                itemRoleView.setText("Admin");
                itemRoleView.setTextColor(Color.RED);

                if(!this.isAdmin(BaseActivity.getUid())){
                    // si je clique sur l'admin et que je ne suis pas cette utilisateur alors
                    dialogInfo(view, i, currentItem);
                }

            }else if(this.isModerator(currentItem.getUid())){
                itemRoleView.setText("Modérateur");
                itemRoleView.setTextColor(Color.parseColor("#388E3C"));

                // On défini les droit en fonction de l'utilisateur connecté

                if(this.isAdmin(BaseActivity.getUid())){
                    // si je click sur un modérateur et que je suis l'admin alors
                    dialogToEditAModerator(view, i, currentItem);
                }else if(!currentItem.getUid().equals(BaseActivity.getUid())){
                    // si je clique sur un modérateur et que je ne suis cette personne
                    dialogInfo(view, i, currentItem);
                }



            }else{
                itemRoleView.setText("Membre");
                itemRoleView.setTextColor(Color.GRAY);

                if(this.isAdmin(BaseActivity.getUid())){
                    // si je click sur un membre et que je suis l'admin alors
                    dialogToEditAMember(view, i, currentItem);
                }else if(this.isModerator(BaseActivity.getUid())){
                    // si je click sur un membre et que je suis modérateur alors
                    dialogToEditAMember(view, i, currentItem);
                }else{
                    // si je click sur un membre et que je suis membre et que ce membre n'est pas moi
                    if(!currentItem.getUid().equals(BaseActivity.getUid())){
                        dialogInfo(view, i, currentItem);
                    }
                }
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
        itemRoleView.setText("Membre");
        itemRoleView.setTextColor(Color.GRAY);
    }

    private void changeToModerator(String uid, View view){
        TextView itemRoleView = view.findViewById(R.id.user_role);
        itemRoleView.setText("Modérateur");
        itemRoleView.setTextColor(Color.parseColor("#388E3C"));
    }

    private void dialogToEditAModerator(View view, int position, User currentItem){
        view.setOnClickListener(v -> {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(currentItem.getUsername());
            // add a list
            String[] actions = {
                    "Contacter",
                    "Rétrograder à membre",
                    "Exclure"};
            builder.setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0: // Contacter
                        Toast.makeText(context, "TO DO !" , Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // Retrograder à simple membre
                        GroupHelper.demoteModeratorToMember(currentGroup.getName(),currentItem.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        changeToMember(currentItem.getUid(), view);
                                        currentGroup.removeToModerators(currentItem.getUid());
                                        dialogToEditAMember(view, position, currentItem);}
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
                                        notifyDataSetChanged();}
                                });
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void dialogToEditAMember(View view, int position, User currentItem){
        view.setOnClickListener(v -> {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(currentItem.getUsername());
            // add a list
            String[] actions = {
                    "Contacter",
                    "Promouvoir",
                    "Exclure"};
            builder.setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0: // Contacter
                        Toast.makeText(context, "TO DO !" , Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // Promouvoir au role de modérateur
                        GroupHelper.promoteMemberToModerator(currentGroup.getName(),currentItem.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        changeToModerator(currentItem.getUid(), view);
                                        currentGroup.addToModerators(currentItem.getUid());
                                        dialogToEditAModerator(view, position, currentItem);}
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
                                        notifyDataSetChanged();}
                                });
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    private void dialogInfo(View view, int position, User currentItem){
        view.setOnClickListener(v -> {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(currentItem.getUsername());
            // add a list
            String[] actions = {
                    // contacter
                    "Contacter"
            };
            builder.setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0: // Contacter
                        Toast.makeText(context, "TO DO !" , Toast.LENGTH_SHORT).show();
                        break;
                } }); // create and show the alert dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

}
