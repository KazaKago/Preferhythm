package com.kazakago.preferhythm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import javax.annotation.Nonnull;

/**
 * アプリ設定情報管理クラス
 *
 * @author Kensuke
 */
public abstract class BasePrefManager implements PrefManager {

    protected final Context context;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public BasePrefManager(@Nonnull Context context) {
        this.context = context;
    }

    protected abstract String getPrefName();

    protected int getMode() {
        return Context.MODE_PRIVATE;
    }

    @Nonnull
    @Override
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences(getPrefName(), getMode());
        return sharedPreferences;
    }

    @Nonnull
    @Override
    @SuppressLint("CommitPrefEdits")
    public SharedPreferences.Editor getEditor() {
        if (editor == null) editor = getSharedPreferences().edit();
        return editor;
    }

    @Nonnull
    @Override
    public PrefManager clear() {
        getEditor().clear();
        return this;
    }

    @Override
    public void apply() {
        getEditor().apply();
    }

    @Override
    public boolean commit() {
        return getEditor().commit();
    }

}
