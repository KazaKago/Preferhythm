package com.kazakago.preferhythm;

import android.content.SharedPreferences;

/**
 * アプリ設定情報管理クラス
 *
 * @author Kensuke
 */
public interface Preferhythm {

    /**
     * SharedPreferencesを取得する
     *
     * @return
     */
    SharedPreferences getSharedPreferences();

    /**
     * SharedPreferences.Editorを取得する
     *
     * @return
     */
    SharedPreferences.Editor getSharedPreferencesEditor();

    /**
     * 保存内容をクリアする
     *
     * @return
     */
    void clear();

    /**
     * 保存内容を確定する
     */
    void apply();

    /**
     * 保存内容を確定する
     *
     * @return
     */
    boolean commit();

}
