package com.cheyrouse.gael.realestatemanagerkt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import static com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.Foreign_Currency;
import static com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.REAL_ESTATE_PREFS;

// SharedPreferences custom class
public class Prefs {

    private static SharedPreferences prefs;
    private static Prefs instance;

    // Get SharedPreferences
    private Prefs(Context context) {
        prefs = context.getSharedPreferences(REAL_ESTATE_PREFS, AppCompatActivity.MODE_PRIVATE);
    }

    // To get instance
    public static Prefs get(Context context) {
        if (instance == null)
            instance = new Prefs(context);
        return instance;
    }

    // Store foreign boolean
    public void storeForeignCurrency(boolean b) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Foreign_Currency, b);
        editor.apply();
    }

    // Get foreign
    public boolean getForeignCurrency() {
        return prefs.getBoolean(Foreign_Currency, false);
    }

}
