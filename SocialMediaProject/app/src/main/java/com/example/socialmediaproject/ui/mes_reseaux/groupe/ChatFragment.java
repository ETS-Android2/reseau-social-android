package com.example.socialmediaproject.ui.mes_reseaux.groupe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.settings.SettingsGroupFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class ChatFragment extends Fragment implements PostAdapter.Listener{

    private PostAdapter postAdapter;
    @Nullable
    Group currentGroup;
    String groupName;
    private User user;
    private RecyclerView recyclerView;
    private ImageView img;

    // Lien image recup entre activité
    private Uri imageUri;
    private StorageReference refImg;

    // pour placer l'image
    private String urlImageToSend;
    private ImageView imageToSend;
    private CardView cardView_imageToSend;
    private Button button_send_message;

    // Couleur du bouton envoyer un message en fonction de si il est clickable ou non
    int colorEnabled ;
    int colorNotEnabled ;



    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_chat, container, false);



        urlImageToSend = "null";
        imageUri = Uri.EMPTY;

        img = root.findViewById(R.id.item_icon);

        // On récupère les paramètres pour voir si le mode sombre est activé
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean modeSombre = settings.getBoolean("theme_dark_mode",false);

        // En fonction du dark mode on change la couleur
        if(modeSombre){
            colorEnabled = ContextCompat.getColor(getContext(), R.color.colorSecondary);
        }else{
            colorEnabled = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        }

        colorNotEnabled = ContextCompat.getColor(getContext(), R.color.colorLight);

        recyclerView = root.findViewById(R.id.recyclerView_group_chat);
        EditText editText_content = root.findViewById(R.id.editText_content_message);

        ImageButton button_load_image = root.findViewById(R.id.button_load_image);
        button_send_message = root.findViewById(R.id.send_message);

        cardView_imageToSend = root.findViewById(R.id.cardView_imageToSend);
        imageToSend = root.findViewById(R.id.imageToSend);

        // On n'affiche pas l'image à envoyé tant qu"il n'y a pas d'image select par le user
        cardView_imageToSend.setVisibility(View.GONE);
        imageToSend.setVisibility(View.GONE);


        // on récupère l'objet du fragment précédent
        Bundle bundle = getArguments();
        if(bundle != null){
            this.groupName = bundle.getString("group_name");

            configureToolbar();
            configureRecyclerView(groupName);


            /**
             *
             * ON CLICK SUR LE BOUTON AJOUTER UNE IMAGE
             */
            button_load_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialisation variables pour l'image
                    imageUri = Uri.EMPTY;
                    urlImageToSend = "null";
                    choosePicture();
                }
            });

            /* picked picture */
            imageToSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    //builder.setTitle("Action");


                    String[] actions = {
                            "Supprimer l'image"
                    };
                    builder.setItems(actions, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                // Choisir une nouvelle image de profile
                                // ON RE INITIALISE LES VARIABLES POUR L'IMAGE
                                imageUri = Uri.EMPTY;
                                imageToSend.setImageResource(0);
                                urlImageToSend = "null";
                                cardView_imageToSend.setVisibility(View.GONE);
                                imageToSend.setVisibility(View.GONE);
                                // on repasse la couleur du bouton en non envoyé si le texte est vide
                                if(editText_content.getText().toString().length() !=0){
                                    button_send_message.setTextColor(colorEnabled);
                                }else{
                                    button_send_message.setTextColor(colorNotEnabled);
                                }
                                break;
                        }
                    });
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });




            // Lorsqu'il y a un changement sur l'edit text
            editText_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // si le text est vide
                    if(s.length() == 0){
                        button_send_message.setTextColor((!Uri.EMPTY.equals(imageUri)) ? colorEnabled : colorNotEnabled);
                    }else{
                        button_send_message.setTextColor(colorEnabled);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });


            /**
             *
             * APPUYER SUR LE BOUTON ENVOYER
             *
             */
            button_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String randomKey = UUID.randomUUID().toString();
                    refImg = BaseActivity.getRefStorage().child("images/" + randomKey);

                    if (!Uri.EMPTY.equals(imageUri)) {
                        // Msg with picture
                        refImg.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //BaseActivity.getRefUser().update("urlPicture", refImg.toString());
                                        urlImageToSend = refImg.toString();

                                        Post message = new Post(editText_content.getText().toString(), groupName, BaseActivity.getUid(), urlImageToSend);
                                        PostHelper.createPostForGroup(message).addOnFailureListener(onFailureListener());

                                        //Toasty.success(getContext(), getContext().getResources().getString(R.string.message_send), Toast.LENGTH_SHORT, true).show();

                                        // On reset
                                        editText_content.setText("");
                                        imageUri = Uri.EMPTY;
                                        urlImageToSend = "null";
                                        cardView_imageToSend.setVisibility(View.GONE);
                                        imageToSend.setVisibility(View.GONE);
                                        button_send_message.setTextColor(colorNotEnabled);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {

                                        Toasty.error(getContext(), "Echec !", Toast.LENGTH_SHORT, false).show();
                                    }
                                });

                    } else if (editText_content.getText().toString().length() != 0) {
                        // Msg without picture
                        Post message = new Post(editText_content.getText().toString(), groupName, BaseActivity.getUid(), "null");
                        PostHelper.createPostForGroup(message).addOnFailureListener(onFailureListener());

                        //Toasty.success(getContext(), getContext().getResources().getString(R.string.message_send), Toast.LENGTH_SHORT, true).show();

                        // On reset
                        editText_content.setText("");
                        imageUri = Uri.EMPTY;
                        urlImageToSend = "null";
                        cardView_imageToSend.setVisibility(View.GONE);
                        imageToSend.setVisibility(View.GONE);
                        button_send_message.setTextColor(colorNotEnabled);
                    } else {

                        Toasty.warning(getContext(), "Veuillez écrire du texte.", Toast.LENGTH_SHORT, false).show();
                    }

                }

            });

        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageToSend.setImageURI(imageUri);
            // on
            cardView_imageToSend.setVisibility(View.VISIBLE);
            imageToSend.setVisibility(View.VISIBLE);
            button_send_message.setTextColor(colorEnabled);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toasty.error(getContext(), "error", Toast.LENGTH_SHORT, false).show();
            }
        };
    }

    public void configureToolbar(){
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // title fragment in the header bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groupName);
    }



    private void configureRecyclerView(String groupName){
        //Configure Adapter & RecyclerView
        this.postAdapter = new PostAdapter(generateOptionsForAdapter(PostHelper.getMessageFromGroup(groupName)),
                Glide.with(this), this, true, true);


        postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(postAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.postAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }


    // --------------------
    // OTHERS
    // --------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
            case R.id.group_menu_settings:
                Bundle bundle = new Bundle();

                bundle.putString("group_name", groupName);
                //NavHostFragment.findNavController(this).navigate(R.id.action_chatFragment_to_settingsGroupFragment, bundle);
                //Navigation.findNavController(this.getView()).navigate(R.id.settingsGroupFragment, bundle);

                SettingsGroupFragment fragment = new SettingsGroupFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty

    }


}