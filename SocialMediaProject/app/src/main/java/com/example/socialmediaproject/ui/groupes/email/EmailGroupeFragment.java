package com.example.socialmediaproject.ui.groupes.email;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaproject.R;

public class EmailGroupeFragment extends Fragment {

    private EmailGroupeViewModel mViewModel;

    public static EmailGroupeFragment newInstance() {
        return new EmailGroupeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groupe_email, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmailGroupeViewModel.class);
        // TODO: Use the ViewModel
    }

}