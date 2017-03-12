package com.kazakago.preferhythm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base preference management class implements Preferhytm.
 *
 * Created by KazaKago on 2017/03/08.
 */
public abstract class BasePreferhythm implements Preferhythm {

    @Nonnull
    protected final Context context;
    @Nullable
    protected SharedPreferences sharedPreferences;
    @Nullable
    protected SharedPreferences.Editor sharedPreferencesEditor;

    public BasePreferhythm(@Nonnull Context context) {
        this.context = context;
    }

    @Nonnull
    @Override
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences(getSharedPreferencesName(), getSharedPreferencesMode());
        return sharedPreferences;
    }

    @Nonnull
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
    @Nonnull
    protected abstract String getSharedPreferencesName();

}
