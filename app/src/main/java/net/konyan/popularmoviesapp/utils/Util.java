package net.konyan.popularmoviesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zeta on 8/14/16.
 */
public class Util {

    public static long getUpdateTime(Context context, String category) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(category, 0);
    }

    public static boolean saveUpdateTime(Context context, String category) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(category, System.currentTimeMillis());
        return editor.commit();
    }
}
