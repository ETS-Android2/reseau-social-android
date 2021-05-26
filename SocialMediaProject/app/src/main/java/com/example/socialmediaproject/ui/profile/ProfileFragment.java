package com.example.socialmediaproject.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.ui.login.LoginActivity;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.ProfileItemAdapter;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.ProfileItem;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView username;
    private User user;
    private CircularImageView img;
    private Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) root.findViewById(R.id.username);
        img = (CircularImageView) root.findViewById(R.id.image_profile);

        UserHelper.getUser(BaseActivity.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                user = task.getResult().toObject(User.class);
                username.setText(user.getUsername());

                Glide.with(requireContext())
                        .load(BaseActivity.getRefImg(user.getUrlPicture()))
                        .into(img);
            }
        });

        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final String createGroup = "Créer un groupe";
        final String myPosts = "Mes posts";
        final String manageMyGroups = "Gérer mes groupes";
        final String myInformations = "Mes informations";

        // list of posts
        List<ProfileItem> profileItemList = new ArrayList<>();
        profileItemList.add(new ProfileItem(createGroup,"add_circle", 0));
        profileItemList.add(new ProfileItem(myPosts,"article", 1));
        profileItemList.add(new ProfileItem(manageMyGroups,"groups", 2));
        profileItemList.add(new ProfileItem(myInformations,"info", 3));

        // get list view
        ListView allPost = (ListView) root.findViewById(R.id.profileListView);
        allPost.setAdapter(new ProfileItemAdapter(getContext(), profileItemList));

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profil");

        // edit profile picture
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //builder.setTitle("Action");

                String change_profile = "Modifier l'image";
                String logout = "Se déconnecter";

                String[] actions = {change_profile, logout};
                builder.setItems(actions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Choisir une nouvelle image de profile
                            choosePicture();
                            break;
                        case 1:
                            if(BaseActivity.isCurrentUserLogged())
                                BaseActivity.getAuth().signOut();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            break;
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


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
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.profile_menu_settings:
                // ouverture de l'activité des paramètres de l'application
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_profile_to_settingsFragment);
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
            uploadPicture();
        }
    }

    public void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Chargement de l'image");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference ref = BaseActivity.getRefStorage().child("images/" + randomKey);

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        BaseActivity.getRefUser().update("urlPicture", ref.toString());

                        Toasty.success(getContext(), "Image chargé !", Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        pd.dismiss();
                        Toasty.error(getContext(), "Échec du chargement" , Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage( (int) progressPercent + "%");
                    }
                });
    }
}