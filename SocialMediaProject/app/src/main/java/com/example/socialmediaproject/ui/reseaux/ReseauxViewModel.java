package com.example.socialmediaproject.ui.reseaux;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReseauxViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReseauxViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}