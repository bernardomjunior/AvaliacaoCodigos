package com.android.pidigits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long before = (new Date()).getTime();
        Log.i("CODE-RUN", "START");
        try {
            Pidigits code = new Pidigits();
            code.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CODE-RUN", "END");
        Log.i("CODE-RUN", "" + ((new Date()).getTime() - before));
    }
}