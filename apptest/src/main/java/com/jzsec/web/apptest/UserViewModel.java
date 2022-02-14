package com.jzsec.web.apptest;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableLiveData;

    public MutableLiveData<List<String>> getLiveData() {
        if (mutableLiveData == null) mutableLiveData = new MutableLiveData<>();
        return mutableLiveData;
    }

    public void getUserList() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("why1");
        strings.add("why2");
        strings.add("why3");
        strings.add("why4");
        getLiveData().postValue(strings);

    }
}
