package com.android.regex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RegexRedux regex = new RegexRedux();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long before = (new Date()).getTime();
        Log.i("CODE-RUN", "START");
        try {
            regex.run(getAssets().open("5000000.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("CODE-RUN", "END");
        Log.i("CODE-RUN", "" + ((new Date()).getTime() - before));
    }
}