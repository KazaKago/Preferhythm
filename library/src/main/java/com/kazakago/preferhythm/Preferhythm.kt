package com.kazakago.preferhythm

import android.content.SharedPreferences

/**
 * Preferhytm Interface.
 *
 *
 * Created by KazaKago on 2017/03/08.
 */
interface Preferhythm {

    /**
     * Get SharedPreferences.
     *
     * @return SharedPreferences
     * @see SharedPreferences
     */
    val sharedPreferences: SharedPreferences

    /**
     * Get SharedPreferences.Editor.
     * @return SharedPreferences.Editor
     * *
     * @see SharedPreferences.Editor
     */
    val sharedPreferencesEditor: SharedPreferences.Editor

    /**
     * clear preferences.
     * @see SharedPreferences.Editor.clear
     */
    fun clear()

    /**
     * apply preferences.
     * @see SharedPreferences.Editor.apply
     */
    fun apply()

    /**
     * commit preferences.
     *
     * @return commit result.
     * @see SharedPreferences.Editor.commit
     */
    fun commit(): Boolean

}
