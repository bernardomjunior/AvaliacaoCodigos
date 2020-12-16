package com.example.emptyrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Repository repo = new Repository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();
    }

    private void request(){
        repo.getTest(response -> {
            Log.i("API RESPONSE", response);
        });
    }

}