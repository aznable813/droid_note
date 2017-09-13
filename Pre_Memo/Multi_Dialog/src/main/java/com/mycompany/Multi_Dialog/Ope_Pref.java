package com.mycompany.Multi_Dialog;
import android.content.*;
import android.preference.*;

public class Ope_Pref{
	Context conte;

    SharedPreferences pref;

    public Ope_Pref(Context context) {
        conte = context;
		pref=PreferenceManager.getDefaultSharedPreferences(conte);
    }

    //データの保存
    public void saveInt(String key, int value) {
		
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //データの取得
    public int getInt(String key, int default_value) {
        return pref.getInt(key, default_value);
    }
    public String getString(String key, String default_value) {
        return pref.getString(key, default_value);
    }
    public boolean getBoolean(String key, boolean default_value) {
        return pref.getBoolean(key, default_value);
    }
}
