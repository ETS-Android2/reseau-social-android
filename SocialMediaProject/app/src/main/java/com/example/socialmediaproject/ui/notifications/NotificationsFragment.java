package com.example.socialmediaproject.ui.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.NotifItemAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Notif;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView textViewRecyclerViewEmpty;
    View finalView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);


        // list of posts
        List<Notif> notifItemList = new ArrayList<>();

        //notifItemList.add(new Notif("Alerte de sécurité"));
        //notifItemList.add(new Notif("Un nouveau membre à été ajouté au groupe"));
        //notifItemList.add(new Notif("Antoine à aimé votre post"));
        //notifItemList.add(new Notif("Salut les gars çava ?"));

        // get list view
        ListView allPost = (ListView) root.findViewById(R.id.notificationsListView);
        allPost.setAdapter(new NotifItemAdapter(getContext(), Lists.reverse(BaseActivity.notifs)));

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.notif_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.notif_menu_params:
                // ouverture de l'activité des paramètres de l'application
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_notifications_to_settingsNotificationFragment);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}