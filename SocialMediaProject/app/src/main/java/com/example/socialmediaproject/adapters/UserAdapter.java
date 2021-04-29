package com.example.socialmediaproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.models.UserHelperClass;

import java.util.List;

/**
 * Created by Antoine Barbier and Antoine Brahimi on 3/30/21.
 */
public class UserAdapter extends BaseAdapter {

    // fields
    private Context context;
    private List<UserHelperClass> userList;
    private LayoutInflater inflater;

    // constructor
    public UserAdapter(Context context, List<UserHelperClass> userList){
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public UserHelperClass getItem(int position) {
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
        UserHelperClass currentItem = getItem(i);
        String itemName = currentItem.getName();

        // get item name view
        TextView itemNameView = (TextView) view.findViewById(R.id.user_name);
        itemNameView.setText(itemName);
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(itemName);
                // add a list
                String[] actions = {"Contacter", "Promouvoir", "Supprimer"};
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Contacter
                                Toast.makeText(context, "Contacter l'utilisateur !"  , Toast.LENGTH_SHORT).show();
                                break;
                            case 1: // Promouvoir
                                Toast.makeText(context, "Promouvoir l'utilisateur !"  , Toast.LENGTH_SHORT).show();
                                break;
                            case 2: // Supprimer
                                Toast.makeText(context, "Partager le post !"  , Toast.LENGTH_SHORT).show();
                                break;
                        } } }); // create and show the alert dialog

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}
