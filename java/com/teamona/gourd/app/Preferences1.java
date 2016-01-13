package com.teamona.gourd.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruzeya on 2014/09/17.
 */
public class Preferences1{
    private SharedPreferences prefs1;
    public static final String PREF_NAME = "Gourd_prefs";

    public Preferences1(Context context){
        prefs1 = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setStringPreference(String key,String value){
        SharedPreferences.Editor editor = prefs1.edit();    //設定ファイルを開いた
        editor.putString(key,value);
        editor.commit();    //書き込み

    }

    public  String getString(String key){
        return prefs1.getString(key,"");
    }


}
