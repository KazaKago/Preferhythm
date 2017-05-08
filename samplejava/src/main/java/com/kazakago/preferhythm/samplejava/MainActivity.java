package com.kazakago.preferhythm.samplejava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText valueEditText1;
    private TextView valueTextView1;
    private EditText valueEditText2;
    private TextView valueTextView2;
    private EditText valueEditText3;
    private TextView valueTextView3;
    private EditText valueEditText4;
    private TextView valueTextView4;

    private MyPreferencesManager myPreferencesManager = new MyPreferencesManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueEditText1 = (EditText) findViewById(R.id.edit_value_1);
        Button setButton1 = (Button) findViewById(R.id.button_set_1);
        setButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.putStringObject(valueEditText1.getText().toString());
                myPreferencesManager.apply();

                valueTextView1.setText(myPreferencesManager.getStringObject());
            }
        });
        Button removeButton1 = (Button) findViewById(R.id.button_remove_1);
        removeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeStringObject();
                myPreferencesManager.apply();

                valueTextView1.setText(myPreferencesManager.getStringObject());
            }
        });
        valueTextView1 = (TextView) findViewById(R.id.text_value_1);
        valueTextView1.setText(myPreferencesManager.getStringObject());

        valueEditText2 = (EditText) findViewById(R.id.edit_value_2);
        Button setButton2 = (Button) findViewById(R.id.button_set_2);
        setButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.putStringObjectNonNull(valueEditText2.getText().toString());
                myPreferencesManager.apply();

                valueTextView2.setText(myPreferencesManager.getStringObjectNonNull());
            }
        });
        Button removeButton2 = (Button) findViewById(R.id.button_remove_2);
        removeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeStringObjectNonNull();
                myPreferencesManager.apply();

                valueTextView2.setText(myPreferencesManager.getStringObjectNonNull());
            }
        });
        valueTextView2 = (TextView) findViewById(R.id.text_value_2);
        valueTextView2.setText(myPreferencesManager.getStringObjectNonNull());

        valueEditText3 = (EditText) findViewById(R.id.edit_value_3);
        Button setButton3 = (Button) findViewById(R.id.button_set_3);
        setButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myPreferencesManager.putIntObject(Integer.valueOf(valueEditText3.getText().toString()));
                    myPreferencesManager.apply();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                valueTextView3.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));
            }
        });
        Button removeButton3 = (Button) findViewById(R.id.button_remove_3);
        removeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeIntObject();
                myPreferencesManager.apply();

                valueTextView3.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));
            }
        });
        valueTextView3 = (TextView) findViewById(R.id.text_value_3);
        valueTextView3.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObject()));

        valueEditText4 = (EditText) findViewById(R.id.edit_value_4);
        Button setButton4 = (Button) findViewById(R.id.button_set_4);
        setButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myPreferencesManager.putIntObjectNonNull(Integer.valueOf(valueEditText4.getText().toString()));
                    myPreferencesManager.apply();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                valueTextView4.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
            }
        });
        Button removeButton4 = (Button) findViewById(R.id.button_remove_4);
        removeButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferencesManager.removeIntObjectNonNull();
                myPreferencesManager.apply();

                valueTextView4.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
            }
        });
        valueTextView4 = (TextView) findViewById(R.id.text_value_4);
        valueTextView4.setText(String.format(Locale.getDefault(), "%d", myPreferencesManager.getIntObjectNonNull()));
    }

}
