package cn.hfiti.toiletapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by static on 2017/11/02.
 *
 */

public class SharedTool {
    private SharedPreferences sharedPreferences;
    private final String SHARE_KEY = "toiletshare";

    public SharedTool(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                SHARE_KEY,context.MODE_WORLD_WRITEABLE);
    }

    public boolean getSharedBoolean(String key,boolean value){
        return sharedPreferences.getBoolean(key,value);
    }

    public String getSharedString(String key,String value){
        return sharedPreferences.getString(key,value);
    }

    public int getSharedInt(String key,int value){
        return sharedPreferences.getInt(key,value);
    }

    public float getSharedFloat(String key, float value){
        return sharedPreferences.getFloat(key,value);
    }

    public void setSharedBoolean(String key,Boolean value){
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void setSharedString(String key,String value){
        Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void setShareInt(String key,int value){
        Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public void setSharedFloat(String key,float value){
        Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.commit();
    }

}
