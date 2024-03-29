package edu.nwmissouri.personalfinancetracker.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public static final String USER_EMAIL = "USER_EMAIL";
    private static final String PREFERENCES_NAME = "AppPreferences";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // String preferences
    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Boolean preferences
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Int preferences
    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Clear preferences
    public static void clearAll(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public static String getUser(Context context) {
        return getString(context, USER_EMAIL, null);
    }

}
