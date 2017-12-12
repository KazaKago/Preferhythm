package com.kazakago.preferhythm.samplekotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val myPreferencesManager = MyPreferencesManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        set1Button.setOnClickListener {
            myPreferencesManager.stringObject = value1EditText.text.toString()
            myPreferencesManager.apply()

            value1TextView.text = myPreferencesManager.stringObject
        }
        remove1Button.setOnClickListener {
            myPreferencesManager.removeStringObject()
            myPreferencesManager.apply()

            value1TextView.text = myPreferencesManager.stringObject
        }
        value1TextView.text = myPreferencesManager.stringObject

        set2Button.setOnClickListener {
            myPreferencesManager.stringObjectNonNull = value2EditText.text.toString()
            myPreferencesManager.apply()

            value2TextView.text = myPreferencesManager.stringObjectNonNull
        }
        remove2Button.setOnClickListener {
            myPreferencesManager.removeStringObjectNonNull()
            myPreferencesManager.apply()

            value2TextView.text = myPreferencesManager.stringObjectNonNull
        }
        value2TextView.text = myPreferencesManager.stringObjectNonNull

        set3Button.setOnClickListener {
            try {
                myPreferencesManager.intObject = Integer.valueOf(value3EditText.text.toString())
                myPreferencesManager.apply()
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            value3TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        remove3Button.setOnClickListener {
            myPreferencesManager.removeIntObject()
            myPreferencesManager.apply()

            value3TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)
        }
        value3TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObject)

        set4Button.setOnClickListener {
            try {
                myPreferencesManager.intObjectNonNull = Integer.valueOf(value4EditText.text.toString())
                myPreferencesManager.apply()
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            value4TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
        }
        remove4Button.setOnClickListener {
            myPreferencesManager.removeIntObjectNonNull()
            myPreferencesManager.apply()

            value4TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
        }
        value4TextView.text = String.format(Locale.getDefault(), "%d", myPreferencesManager.intObjectNonNull)
    }
}
