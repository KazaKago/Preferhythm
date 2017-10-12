package com.kazakago.preferhythm

import android.content.Context
import android.content.SharedPreferences

/**
 * Base preference management class implements Preferhytm.
 *
 * Created by KazaKago on 2017/03/08.
 */
abstract class BasePreferhythm(protected val context: Context) : Preferhythm {

    override val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPreferencesName, sharedPreferencesMode)
    }

    override val sharedPreferencesEditor: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }

    override fun clear() {
        sharedPreferencesEditor.clear()
    }

    override fun apply() {
        sharedPreferencesEditor.apply()
    }

    override fun commit(): Boolean {
        return sharedPreferencesEditor.commit()
    }

    /**
     * Get SharedPreferences Mode.
     *
     * @return Context.MODE_...
     */
    var sharedPreferencesMode = Context.MODE_PRIVATE

    /**
     * Get SharedPreferences Name.
     *
     * @return Preferences filename.
     */
    abstract val sharedPreferencesName: String

}
