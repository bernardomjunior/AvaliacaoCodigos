package com.example.emptyrequest;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Repository {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/rgrove/rawgit/master/.gitignore/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private final Api api = retrofit.create(Api.class);

     public void getTest(
             RequestCallBack callBack
     ){
         Call<String> call = api.test();
         call.enqueue(new Callback<String>() {
             @Override
             public void onResponse(Call<String> call, Response<String> response) {
                 if (response.isSuccessful()){
                     callBack.onResponse(response.body());
                 } else {
                     Log.e("REQUEST", "ERROR" + response.body());
                 }
             }

             @Override
             public void onFailure(Call<String> call, Throwable t) {
                 Log.e("REQUEST", "FAILURE" + t.getMessage());
             }
         });
     }

}

interface RequestCallBack{

    void onResponse(String response);

}
