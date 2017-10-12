package com.kazakago.preferhythm.samplekotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private val myPreferencesManager = MyPreferencesManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val valueEditText1 = findViewById<EditText>(R.id.edit_value_1)
        val valueTextView1 = findViewById<TextView>(R.id.text_value_1)
        val setButton1 = findViewById<Button>(R.id.button_set_1)
        setButton1.setOnClickListener {
            myPreferencesManager.putStringObject(valueEditText1.text.toString())
            myPreferencesManager.apply()

            valueTextView1.text = myPreferencesManager.stringObject
        }
        val removeButton1 = findViewById<Button>(R.id.button_remove_1)
        removeButton1.setOnClickListener {
            myPreferencesManager.removeStringObject()
            myPreferencesManager.apply()

            valueTextView1.text = myPreferencesManager.stringObject
        }
        valueTextView1.text = myPreferencesManager.stringObject

        val valueEditText2 = findViewById<EditText>(R.id.edit_value_2)
        val valueTextView2 = findViewById<TextView>(R.id.text_value_2)
        val setButton2 = findViewById<Button>(R.id.button_set_2)
        setButton2.setOnClickListener {
            myPreferencesManager.putStringObjectNonNull(valueEditText2.text.toString())
            myPreferencesManager.apply()

            valueTextView2.text = myPreferencesManager.stringObjectNonNull
        }
        val removeButton2 = findViewById<Button>(R.id.button_remove_2)
        removeButton2.setOnClickListener {
            myPreferencesManager.removeStringObjectNonNull()
            myPreferencesManager.apply()

            valueTextView2.text = myPreferencesManager.stringObjectNonNull
        }
        valueTextView2.text = myPreferencesManager.stringObjectNonNull

        val valueEditText3 = findViewById<EditText>(R.id.edit_value_3)
        val valueTextView3 = findViewById<TextView>(R.id.text_value_3)
        val setButton3 = findViewById<Button>(R.id.button_set_3)
        setButton3.setOnClickListener {
            try {
                myPreferencesManager.putIntObject(Integer.valueOf(valueEditText3.text.toString()))
                myPreferencesManager.apply()
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        val removeButton3 = findViewById<Button>(R.id.button_remove_3)
        removeButton3.setOnClickListener {
            myPreferencesManager.removeIntObject()
            myPreferencesManager.apply()

            valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)

        val valueEditText4 = findViewById<EditText>(R.id.edit_value_4)
        val valueTextView4 = findViewById<TextView>(R.id.text_value_4)
        val setButton4 = findViewById<Button>(R.id.button_set_4)
        setButton4.setOnClickListener {
            try {
                myPreferencesManager.putIntObjectNonNull(Integer.valueOf(valueEditText4.text.toString()))
                myPreferencesManager.apply()
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
        }
        val removeButton4 = findViewById<Button>(R.id.button_remove_4)
        removeButton4.setOnClickListener {
            myPreferencesManager.removeIntObjectNonNull()
            myPreferencesManager.apply()

            valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
        }
        valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
    }
}
