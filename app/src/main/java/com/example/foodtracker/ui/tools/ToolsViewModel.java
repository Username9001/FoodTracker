package com.example.foodtracker.ui.tools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToolsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ToolsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Use the camera to scan a barcode or look up a product using the search function");

    }

    public LiveData<String> getText() {
        return mText;
    }
}