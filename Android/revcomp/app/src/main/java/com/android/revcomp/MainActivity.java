package com.android.revcomp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RevComp code = new RevComp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long startTime = System.currentTimeMillis();
        try {
            code.run(getAssets().open("10000000.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/1000;
        Log.i("CODE TIME", ""+duration);
    }
}