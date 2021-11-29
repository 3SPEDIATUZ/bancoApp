package com.example.bancoapp_mejorado.ui.retirar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RetirarViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RetirarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is retirar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
