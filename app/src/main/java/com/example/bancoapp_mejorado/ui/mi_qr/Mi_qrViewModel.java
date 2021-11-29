package com.example.bancoapp_mejorado.ui.mi_qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Mi_qrViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public Mi_qrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is qr fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
