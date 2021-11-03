package com.android.revcomp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long before = (new Date()).getTime();
        Log.i("CODE-RUN", "START");
        try {
            RevComp code = new RevComp();
            code.run(getAssets().open("10000000.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CODE-RUN", "END");
        Log.i("CODE-RUN", "" + ((new Date()).getTime() - before));
    }
}