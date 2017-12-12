package com.kazakago.preferhythm.samplejava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MyPreferencesManager myPreferencesManager = new MyPreferencesManager(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText value1EditText = findViewById(R.id.value1EditText);
        final TextView value1TextView = findViewById(R.id.value1TextView);
        Button set1Button = findViewById(R.id.set1Button);
        set1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.setStringObject(value1EditText.getText().toString());
                myPreferencesManager.apply();

                value1TextView.setText(myPreferencesManager.getStringObject());
            }
        });
        Button remove1Button = findViewById(R.id.remove1Button);
        remove1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeStringObject();
                myPreferencesManager.apply();

                value1TextView.setText(myPreferencesManager.getStringObject());
            }
        });
        value1TextView.setText(myPreferencesManager.getStringObject());

        final EditText value2EditText = findViewById(R.id.value2EditText);
        final TextView value2TextView = findViewById(R.id.value2TextView);
        Button set2Button = findViewById(R.id.set2Button);
        set2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.setStringObjectNonNull(value2EditText.getText().toString());
                myPreferencesManager.apply();

                value2TextView.setText(myPreferencesManager.getStringObjectNonNull());
            }
        });
        Button remove2Button = findViewById(R.id.remove2Button);
        remove2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeStringObjectNonNull();
                myPreferencesManager.apply();

                value2TextView.setText(myPreferencesManager.getStringObjectNonNull());
            }
        });
        value2TextView.setText(myPreferencesManager.getStringObjectNonNull());

        final EditText value3EditText = findViewById(R.id.value3EditText);
        final TextView value3TextView = findViewById(R.id.value3TextView);
        Button set3Button = findViewById(R.id.set3Button);
        set3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myPreferencesManager.setIntObject(Integer.valueOf(value3EditText.getText().toString()));
                    myPreferencesManager.apply();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                value3TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));
            }
        });
        Button remove3Button = findViewById(R.id.remove3Button);
        remove3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeIntObject();
                myPreferencesManager.apply();

                value3TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));
            }
        });
        value3TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));

        final EditText value4EditText = findViewById(R.id.value4EditText);
        final TextView value4TextView = findViewById(R.id.value4TextView);
        Button set4Button = findViewById(R.id.set4Button);
        set4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myPreferencesManager.setIntObjectNonNull(Integer.valueOf(value4EditText.getText().toString()));
                    myPreferencesManager.apply();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                value4TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
            }
        });
        Button remove4Button = findViewById(R.id.remove4Button);
        remove4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeIntObjectNonNull();
                myPreferencesManager.apply();

                value4TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
            }
        });
        value4TextView.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
    }

}
