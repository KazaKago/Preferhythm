package com.kazakago.preferhythm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Base preference management class implements Preferhytm.
 * <p>
 * Created by KazaKago on 2017/03/08.
 */
public abstract class BasePreferhythm implements Preferhythm {

    @NonNull
    protected final Context context;
    @Nullable
    protected SharedPreferences sharedPreferences;
    @Nullable
    protected SharedPreferences.Editor sharedPreferencesEditor;

    public BasePreferhythm(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences(getSharedPreferencesName(), getSharedPreferencesMode());
        return sharedPreferences;
    }

    @NonNull
    @Override
    @SuppressLint("CommitPrefEdits")
    public SharedPreferences.Editor getSharedPreferencesEditor() {
        if (sharedPreferencesEditor == null) sharedPreferencesEditor = getSharedPreferences().edit();
        return sharedPreferencesEditor;
    }

    @Override
    public void clear() {
        getSharedPreferencesEditor().clear();
    }

    @Override
    public void apply() {
        getSharedPreferencesEditor().apply();
    }

    @Override
    public boolean commit() {
        return getSharedPreferencesEditor().commit();
    }

    /**
     * Get SharedPreferences Mode.
     *
     * @return Context.MODE_...
     */
    protected int getSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }

    /**
     * Get SharedPreferences Name.
     *
     * @return Preferences filename.
     */
    @NonNull
    protected abstract String getSharedPreferencesName();

}
