package com.example.socialmediaproject.ui.pagesprofile.favoris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaproject.R;

public class MyFavorisFragment extends Fragment {

    private MyFavorisViewModel mViewModel;

    public static MyFavorisFragment newInstance() {
        return new MyFavorisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.my_favoris_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes favoris");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyFavorisViewModel.class);
        // TODO: Use the ViewModel
    }

}