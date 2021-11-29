package com.example.bancoapp_mejorado.ui.transferir;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransferirViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TransferirViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is transferir fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
