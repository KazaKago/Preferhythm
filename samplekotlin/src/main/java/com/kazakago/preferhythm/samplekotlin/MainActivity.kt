package com.kazakago.preferhythm.samplekotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private val valueEditText1 by lazy { findViewById(R.id.edit_value_1) as EditText }
    private val valueTextView1 by lazy { findViewById(R.id.text_value_1) as TextView }
    private val valueEditText2 by lazy { findViewById(R.id.edit_value_2) as EditText }
    private val valueTextView2 by lazy { findViewById(R.id.text_value_2) as TextView }
    private val valueEditText3 by lazy { findViewById(R.id.edit_value_3) as EditText }
    private val valueTextView3 by lazy { findViewById(R.id.text_value_3) as TextView }
    private val valueEditText4 by lazy { findViewById(R.id.edit_value_4) as EditText }
    private val valueTextView4 by lazy { findViewById(R.id.text_value_4) as TextView }

    val myPreferencesManager = MyPreferencesManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setButton1 = findViewById(R.id.button_set_1) as Button
        setButton1.setOnClickListener {
            myPreferencesManager.putStringObject(valueEditText1.text.toString())
            myPreferencesManager.apply()

            valueTextView1.text = myPreferencesManager.stringObject
        }
        val removeButton1 = findViewById(R.id.button_remove_1) as Button
        removeButton1.setOnClickListener {
            myPreferencesManager.removeStringObject()
            myPreferencesManager.apply()

            valueTextView1.text = myPreferencesManager.stringObject
        }
        valueTextView1.text = myPreferencesManager.stringObject

        val setButton2 = findViewById(R.id.button_set_2) as Button
        setButton2.setOnClickListener {
            myPreferencesManager.putStringObjectWithInit(valueEditText2.text.toString())
            myPreferencesManager.apply()

            valueTextView2.text = myPreferencesManager.stringObjectWithInit
        }
        val removeButton2 = findViewById(R.id.button_remove_2) as Button
        removeButton2.setOnClickListener {
            myPreferencesManager.removeStringObjectWithInit()
            myPreferencesManager.apply()

            valueTextView2.text = myPreferencesManager.stringObjectWithInit
        }
        valueTextView2.text = myPreferencesManager.stringObjectWithInit

        val setButton3 = findViewById(R.id.button_set_3) as Button
        setButton3.setOnClickListener {
            myPreferencesManager.putIntObject(Integer.valueOf(valueEditText3.text.toString()))
            myPreferencesManager.apply()

            valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        val removeButton3 = findViewById(R.id.button_remove_3) as Button
        removeButton3.setOnClickListener {
            myPreferencesManager.removeIntObject()
            myPreferencesManager.apply()

            valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        valueTextView3.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)

        val setButton4 = findViewById(R.id.button_set_4) as Button
        setButton4.setOnClickListener {
            try {
                myPreferencesManager.putIntObjectWithInit(Integer.valueOf(valueEditText4.text.toString()))
                myPreferencesManager.apply()
            } catch (e: ClassCastException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectWithInit)
        }
        val removeButton4 = findViewById(R.id.button_remove_4) as Button
        removeButton4.setOnClickListener {
            try {
                myPreferencesManager.removeIntObjectWithInit()
                myPreferencesManager.apply()
            } catch (e: ClassCastException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectWithInit)
        }
        valueTextView4.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectWithInit)
    }
}
