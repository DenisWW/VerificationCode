package com.nineone.verificationcode.bean;


import com.example.annotation.GetAnnotation;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetTest {
    @GetAnnotation(v = "就是想看看")
    public void getTest(String s);

    @GET("/")
    Call<String> getBaidu();
}
