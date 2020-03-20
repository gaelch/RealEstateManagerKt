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

    public void storeItemViewList(List<View> views) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(views);
        editor.putString("itemView", json);
        editor.apply();
    }

    public ArrayList<View> getViewList() {
        Gson gson = new Gson();
        String json = prefs.getString("itemView", "");
        ArrayList<View> viewList;
        if (json.length() < 1) {
            viewList = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<View>>() {
            }.getType();
            viewList = gson.fromJson(json, type);
        }
        //return the value that was stored under the key
        return viewList;
    }

}
