package com.example.emptyrequest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("what_now/")
    Call<String> started();

    @GET("log_data/")
    Call<String> finished();

    @GET("/")
    Call<String> test();

}