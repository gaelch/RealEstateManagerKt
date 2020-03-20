package com.cheyrouse.gael.realestatemanagerkt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Prefs {
    private static final String MY_PREFS = "my_prefs";

    //This class using SharedPreferences and the Gson library

    private static Prefs instance;
    private static SharedPreferences prefs;


    //Class Prefs constructor
    private Prefs(Context context) {
        prefs = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    //Prefs.get is called to create a new instance of Prefs
    public static Prefs get(Context context) {
        if (instance == null)
            instance = new Prefs(context);
        return instance;
    }

    public void storeLastItemClicked(int item){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("item", item);
        editor.apply();
    }

    public int getLastItemClicked(){
        return prefs.getInt("item", 0);
    }

}
