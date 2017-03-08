package com.kazakago.quickpref;

import android.content.SharedPreferences;

import java.util.Set;

/**
 * SharedPreferencesを扱うWrapperクラス<br>
 * 基本型もオブジェクト型として返却するため項目がnullの場合にはnullを返す
 *
 * @author KCMEUser
 */
public class SharedPreferencesWrapper {

    public static Integer getInteger(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getInt(name, 0) : null;
    }

    public static SharedPreferences.Editor putInteger(SharedPreferences.Editor edit, String name, Integer value) {
        return (value != null) ? edit.putInt(name, value) : edit.remove(name);
    }

    public static Long getLong(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getLong(name, 0) : null;
    }

    public static SharedPreferences.Editor putLong(SharedPreferences.Editor edit, String name, Long value) {
        return (value != null) ? edit.putLong(name, value) : edit.remove(name);
    }

    public static Float getFloat(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getFloat(name, 0) : null;
    }

    public static SharedPreferences.Editor putFloat(SharedPreferences.Editor edit, String name, Float value) {
        return (value != null) ? edit.putFloat(name, value) : edit.remove(name);
    }

    public static Boolean getBoolean(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getBoolean(name, false) : null;
    }

    public static SharedPreferences.Editor putBoolean(SharedPreferences.Editor edit, String name, Boolean value) {
        return (value != null) ? edit.putBoolean(name, value) : edit.remove(name);
    }

    public static String getString(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getString(name, null) : null;
    }

    public static SharedPreferences.Editor putString(SharedPreferences.Editor edit, String name, String value) {
        return (value != null) ? edit.putString(name, value) : edit.remove(name);
    }

    public static Set<String> getStringSet(SharedPreferences pref, String name) {
        return (pref.contains(name)) ? pref.getStringSet(name, null) : null;
    }

    public static SharedPreferences.Editor putStringSet(SharedPreferences.Editor edit, String name, Set<String> value) {
        return (value != null) ? edit.putStringSet(name, value) : edit.remove(name);
    }

}
