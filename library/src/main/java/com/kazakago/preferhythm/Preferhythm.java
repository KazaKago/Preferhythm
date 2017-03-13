package com.kazakago.preferhythm;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Preferhytm Interface.
 * <p>
 * Created by KazaKago on 2017/03/08.
 */
public interface Preferhythm {

    /**
     * Get SharedPreferences.
     *
     * @return SharedPreferences
     * @see SharedPreferences
     */
    @NonNull
    SharedPreferences getSharedPreferences();

    /**
     * Get SharedPreferences.Editor.
     *
     * @return SharedPreferences.Editor
     * @see SharedPreferences.Editor
     */
    @NonNull
    SharedPreferences.Editor getSharedPreferencesEditor();

    /**
     * clear preferences.
     *
     * @see SharedPreferences.Editor#clear()
     */
    void clear();

    /**
     * apply preferences.
     *
     * @see SharedPreferences.Editor#apply()
     */
    void apply();

    /**
     * commit preferences.
     *
     * @return commit result.
     * @see SharedPreferences.Editor#commit()
     */
    boolean commit();

}
