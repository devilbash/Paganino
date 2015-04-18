
package it.bestapp.paganino.utility.setting;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    public static final String SHARED_PREFERENCES_NAME = "PAGANINO_PREFERENCES";

    private SharedPreferences sharedPreferences;


    protected PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setBoolean(Integer name, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name.toString(), value);
        editor.commit();
    }

    public boolean getBoolean(Integer name) {
        return sharedPreferences.getBoolean(name.toString(), false);
    }

    public void setString(Integer name, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name.toString(), value);
        editor.commit();
    }

    public String getString(Integer name) {
        return sharedPreferences.getString(name.toString(), "");
    }

    public void setInt(Integer name, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name.toString(), value);
        editor.commit();
    }

    public int getInt(Integer name) {
        return sharedPreferences.getInt(name.toString(), 0);
    }


}
