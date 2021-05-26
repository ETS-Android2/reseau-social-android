package com.example.socialmediaproject.adapters;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.ui.mes_reseaux.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Created by Antoine Barbier on 5/15/21.
 */

public class SearchGroupAdapter extends FirestoreRecyclerAdapter<Group, SearchGroupAdapter.MyViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    protected Context context;

    //FOR DATA
    private final RequestManager glide;

    //FOR COMMUNICATION
    private Listener callback;


    // constructor
    public SearchGroupAdapter(@NonNull FirestoreRecyclerOptions<Group> options,
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

            // Set Group Type View
            if(currentItem.getType() != null){
                itemGroupTypeView.setTextColor(Color.GRAY);
                itemGroupTypeView.setText(currentItem.getType().toUpperCase());
            }


            boolean alreadyInGroup = currentItem.getMembers().contains(BaseActivity.getUid());
            boolean alreadyInGroupWaitingList = currentItem.getWaitlist().contains(BaseActivity.getUid());

            if(alreadyInGroup){
                // si l'utilisateur est déja dans le groupe
                button_add.setEnabled(true);
                //itemIconView.setColorFilter(ContextCompat.getColor(context, R.color.colorSecondary),PorterDuff.Mode.MULTIPLY);
                button_add.setText("Voir");
                button_add.setOnClickListener(new View.OnClickListener() {
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
                            Navigation.findNavController(itemView).navigate(R.id.action_searchPageFragment_to_navigation_groupe, bundle);
                        }
                    }
                });

            }else if(alreadyInGroupWaitingList){
                // si l'utilisateur à déjà fait une demande
                button_add.setEnabled(false);
                button_add.setText("En attente");
            }else{
                // alors on affiche le bouton d'ajout du groupe pour envoyé une demande d'adhésion
                button_add.setEnabled(true);
                button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupHelper.addUserInWaitlistGroup(currentItem.getName(),BaseActivity.getUid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(context, context.getResources().getString(R.string.toast_send_request_waitinglist), Toast.LENGTH_SHORT).show();

                                        /**********************/
                                        /* DÉBUT NOTIFICATION */
                                        /**********************/

                                        /*
                                        *   1. On place un écouteur d'événement sur la liste des membres d'un groupe
                                        *   2. On récupère cette liste et on regarde si on est dedans
                                        *   3. Si c'est le cas alors on s'abonne au canal de communication (qui correspond au nom du groupe en minuscule et sans espace)
                                        *   4. Lorsqu'on quitte un groupe ou on se fait exclure, on se désabonne du canal de communication
                                        */

                                        GroupHelper.getMembersListOfGroup(group.getName()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                                ArrayList<String> listMembre = (ArrayList<String>) value.getData().get("members");

                                                Log.d("LES MEMBRES SONT : ", listMembre.toString());

                                                String groupNameWithoutSpace = group.getName().replaceAll("\\s+","").toLowerCase();
                                                Log.d("Group abrégé : ", groupNameWithoutSpace);

                                                if(listMembre.contains(BaseActivity.getUid())){
                                                    BaseActivity.getMessaging().subscribeToTopic(groupNameWithoutSpace)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) { }
                                                            });

                                                }
                                                else{
                                                    BaseActivity.getMessaging().unsubscribeFromTopic(groupNameWithoutSpace)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) { }
                                                            });
                                                }
                                            }
                                        });

                                        /********************/
                                        /* FIN NOTIFICATION */
                                        /********************/
                                    }
                                });
                    }
                });
            }
        }
    }
}
