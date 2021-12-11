package com.android.binarytree;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("/what_now")
    Call<String> started();

    @GET("/logdata")
    Call<String> finished();

    @GET("/")
    Call<String> test();

}
