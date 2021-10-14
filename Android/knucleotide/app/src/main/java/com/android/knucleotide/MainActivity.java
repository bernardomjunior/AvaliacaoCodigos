package com.android.knucleotide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String text;
        Log.i("CODE-RUN", "START");
        try {
            text = readAssets();
            boolean a = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("CODE-RUN", "END");
    }

    private String readAssets() throws IOException {
        StringBuilder completeFile = new StringBuilder();
        BufferedReader reader = null;
        reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("knucleotide input - 2500000.txt")));
        String mLine;
        while ((mLine = reader.readLine()) != null) {
            completeFile.append(mLine);
        }
        reader.close();
        return completeFile.toString();
    }


}