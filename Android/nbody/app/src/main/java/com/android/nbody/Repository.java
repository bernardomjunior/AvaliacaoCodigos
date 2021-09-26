package com.android.nbody;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Repository {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.101:3000/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private final Api api = retrofit.create(Api.class);

    public void start(RequestCallBack callBack){
        Call<String> call = api.started();
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

    public void finish(){
        Call<String> call = api.finished();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log.i("REQUEST", "SUCCESS" + response.body());
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