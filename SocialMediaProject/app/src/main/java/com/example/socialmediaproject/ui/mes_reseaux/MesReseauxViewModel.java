package com.example.socialmediaproject.ui.mes_reseaux;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MesReseauxViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MesReseauxViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}