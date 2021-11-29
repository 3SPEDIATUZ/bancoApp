package com.example.bancoapp_mejorado.ui.transferir_qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Transferir_qrViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public Transferir_qrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is transferir_qr fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
