package com.kazakago.preferhythm.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kazakago.quickpref.sample.MyPreferencesManager;
import com.kazakago.quickpref.sample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyPreferencesManager manager = new MyPreferencesManager(this);
    }
}
