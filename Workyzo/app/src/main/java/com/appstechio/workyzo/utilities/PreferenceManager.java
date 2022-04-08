package com.appstechio.workyzo.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PreferenceManager {

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){
        sharedPreferences =context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,context.MODE_PRIVATE);

    }

    public void putInt (String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }

    public void putDouble (String key, Float value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.apply();
    }
    public float getDouble(String key){
        return sharedPreferences.getFloat(key,0);
    }

    public void putBoolean (String key, Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void putKey(String key, PrivateKey value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public void putStringArray(String key, ArrayList<String> value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public ArrayList<String> getStringArray(String key){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        Type type = new TypeToken<List<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return  arrayList;
    }

    public void putMapArray(String key, ArrayList<HashMap> value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public void removeMapArray(String key, ArrayList<HashMap> value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.remove(json);
        editor.commit();
    }

    public ArrayList<HashMap> getMapArray(String key){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        Type type = new TypeToken<List<HashMap>>() {}.getType();
        ArrayList<HashMap> arrayList = gson.fromJson(json, type);
        return  arrayList;
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public String getKey(String key){
        return sharedPreferences.getString(key,null);
    }


    public void Clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
